package com.metech.firefly.api;

import retrofit.Endpoint;

public class ApiEndpoint implements Endpoint {

    @Override
    public String getUrl() {

       //return "https://m.fireflyz.com.my/api";
       return "http://fyapidev.me-tech.com.my/api";

    }

    @Override
    public String getName() {
        return "Production Endpoint";
    }
    //
}

//http://sheetsu.com/apis/c4182617