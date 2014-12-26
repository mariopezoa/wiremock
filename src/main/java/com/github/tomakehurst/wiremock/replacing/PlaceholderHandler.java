package com.github.tomakehurst.wiremock.replacing;

import com.github.tomakehurst.wiremock.http.Response;

/**
 * Created by mpezoa on 12/16/14.
 */
public class PlaceholderHandler {
    public PlaceholderReplacer[] replacers = {new DatePlaceholderReplacer()};
    private Response response;

    public PlaceholderHandler(Response response){
        this.response = response;
    }

    public Response handle(){
        int size = replacers.length;
        for(int i=0; i<size; i++){
            response = replacers[i].replaceAll(response);
        }
        return response;
    }
}
