package com.example.dk88;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUserRequester
{
    private static JsonPlaceHolderApi jsonPlaceHolderApi;
    public static JsonPlaceHolderApi getJsonPlaceHolderApi() {
        if (jsonPlaceHolderApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.3.33:6969/api/v1/User/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        }
        return jsonPlaceHolderApi;
    }
}
