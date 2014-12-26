package com.github.tomakehurst.wiremock.replacing;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.github.tomakehurst.wiremock.common.LocalNotifier.notifier;

/**
 * Created by mpezoa on 12/16/14.
 */
public class DatePlaceholderReplacer extends PlaceholderReplacer {


    public DatePlaceholderReplacer(){
        super("(DATE\\(([\\+|-][0-9]+[h|d|m|y]),([0-9YyMmDdHhSsTtaEZz\\:\\/\\-\\'\\ ,]+)\\))");
    }

    @Override
    public String eval(String[] arguments) {
        String res;
        if(arguments.length!=3) {
            notifier().error("An incorrect number of arguments was passed to DATE function");
            res = "";
        }
        else{
            try {
                Calendar c = Calendar.getInstance();
                String increment = arguments[0].replace("+", "");
                int numericIncrement = Integer.valueOf(increment);

                String dateOption = arguments[1].toLowerCase();
                char option = dateOption.charAt(2);

                c.add(getOption(option), numericIncrement);

                SimpleDateFormat dateFormatter = new SimpleDateFormat(arguments[2]);
                res = dateFormatter.format(c.getTime());
            }
            catch(Exception ex){
                notifier().error("An error occurred when replacing a DATE in DatePlaceholderReplacer: " + ex.getMessage());
                res = "";
            }
        }

        return res;
    }

    private int getOption(char opt){
        int res =-1;
        switch (opt){
            case 'd':{
                res = Calendar.DATE;
                break;
            }
            case 'h':{
                res = Calendar.HOUR_OF_DAY;
                break;
            }
            case 'm':{
                res = Calendar.MONTH;
                break;
            }
            case 'y':{
                res=Calendar.YEAR;
                break;
            }
        }
        return res;
    }
}
