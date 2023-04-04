package com.example.dk88;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface JsonPlaceHolderApi {

    @GET(".")
    Call<ResponseObject> test();

    @POST("Login")
    Call<ResponseObject> login(@Body Map<String, Object> body);

}
