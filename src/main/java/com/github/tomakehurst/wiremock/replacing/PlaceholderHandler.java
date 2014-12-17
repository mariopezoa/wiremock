package com.github.tomakehurst.wiremock.replacing;

import com.github.tomakehurst.wiremock.http.Response;

import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;

/**
 * Created by mpezoa on 12/16/14.
 */
public class PlaceholderHandler {
    public PlaceholderReplacer[] replacers = {new DatePlaceholderReplacer()};
    private Response response;

    public PlaceholderHandler(Response response){
        notifier().info("MPJ: Constructor de PlaceHolderHanlder");
        this.response = response;
    }

    public Response handle(){
        notifier().info("MPJ: handle method");
        int size = replacers.length;
        for(int i=0; i<size; i++){
            notifier().info("MPJ: handle "+ i);
            response = replacers[i].replaceAll(response);
        }
        return response;
    }
}
