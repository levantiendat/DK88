package com.example.dk88;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUserRequester
{
    private static JsonPlaceHolderApi jsonPlaceHolderApi;
    public static JsonPlaceHolderApi getJsonPlaceHolderApi() {
        if (jsonPlaceHolderApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://ec2-13-229-209-65.ap-southeast-1.compute.amazonaws.com:6969/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        }
        return jsonPlaceHolderApi;
    }
}
