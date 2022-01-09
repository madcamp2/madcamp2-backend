package com.example.everytask.trivialModules;

import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

public class MyHttpClient {
    private static String baseURL = "https://kauth.kakao.com";
    private static String restApiKey = "745ffe68d06ddbf22efb96dc9fc84f47";
    private static String redirectUri = "http://192.249.18.137/oauth";

    private MyHttpClient(){}

    public static void getAccessToken(String authorizationCode){
        OkHttp3ClientHttpRequestFactory client = new OkHttp3ClientHttpRequestFactory();
//        client.
    }

}
