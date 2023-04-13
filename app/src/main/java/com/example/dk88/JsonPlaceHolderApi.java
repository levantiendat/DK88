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

    @POST("CreateAccountStudent")
    Call<ResponseObject> signup(@Body Map<String,Object> body);

    @POST("ChangePassword")
    Call<ResponseObject> changePass(@Body Map<String,Object> body);

    @POST("ChangePublicInfo")
    Call<ResponseObject> changeProfile(@Body Map<String,Object> body);
}
