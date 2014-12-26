package com.github.tomakehurst.wiremock.replacing;

import com.github.tomakehurst.wiremock.http.Response;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;

/**
 * Created by mpezoa on 12/16/14.
 */
public abstract class PlaceholderReplacer {

    String placeholderPattern;
    private Pattern pattern;

    public PlaceholderReplacer(String p){
        this.placeholderPattern = p;
        pattern = Pattern.compile(this.placeholderPattern);
    }
    abstract String eval(String[] arguments);

    public Response replaceAll(Response r) {
        String body = r.getBodyAsString();
        Matcher m = pattern.matcher(body);
        StringBuffer newBody= new StringBuffer();
        while (m.find()) {
            int groups = m.groupCount()-1;
            String[] arguments = new String[groups];
            for (int i = 0; i < groups; i++) {
                //groups in regexp are 1-based index
                arguments[i] = m.group(i+2);
            }
            String computedValue = eval(arguments);
            notifier().info("valor a reemplazar ["+computedValue+"] para expresion ["+m.toString()+"]");
            m.appendReplacement(newBody, computedValue);
        }
        m.appendTail(newBody);
        r.setBody(newBody.toString());
        return r;
    }

}
