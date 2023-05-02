package com.example.dk88;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET(".")
    Call<ResponseObject> test();

    @POST("Login")
    Call<ResponseObject> login(@Body Map<String, Object> body);

    @POST("CreateAccountStudent")
    Call<ResponseObject> signup(@Body Map<String,Object> body);

    @Headers("Content-Type: application/json")
    @POST("ChangePassword")
    Call<ResponseObject> changePass(@Header("token") String token, @Body Map<String,Object> body);

    @POST("ChangePublicInfo")
    Call<ResponseObject> changeProfile(@Body Map<String,Object> body);

    @POST("Request/Active")
    Call<ResponseObject> sendActiveRequest(@Body Map<String,Object> body);

    @POST("Request/Ban")
    Call<ResponseObject> sendBanRequest(@Body Map<String,Object> body);

    @GET ("Request/Page/{page}")
    Call<ResponseObject> readRequestPage(@Path("page") int pageID);

    @POST("Request/Detail")
    Call<ResponseObject> readDetailRequest(@Body Map<String,Object> body);

    @POST("Request/Handle")
    Call<ResponseObject> handleRequest(@Body Map<String,Object> body);


}
