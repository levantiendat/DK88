package com.example.dk88;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface JsonPlaceHolderApi {

    @GET(".")
    Call<ResponseObject> test();

    @POST("User/Login")
    Call<ResponseObject> login(@Body Map<String, Object> body);

    @POST("User/CreateAccountStudent")
    Call<ResponseObject> signup(@Body Map<String,Object> body);

    // @Headers("Content-Type: application/json")
    @POST("User/ChangePassword")
    Call<ResponseObject> changePass(@HeaderMap Map<String,Object> header, @Body Map<String,Object> body);

    @POST("User/ChangePublicInfo")
    Call<ResponseObject> changeProfile(@HeaderMap Map<String,Object> header, @Body Map<String,Object> body);

    @POST("Request/Active")
    Call<ResponseObject> sendActiveRequest(@HeaderMap Map<String,Object> header,@Body Map<String,Object> body);

    @POST("Request/Ban")
    Call<ResponseObject> sendBanRequest(@HeaderMap Map<String,Object> header,@Body Map<String,Object> body);

    @GET ("Request/Page/{page}")
    Call<ResponseObject> readRequestPage(@HeaderMap Map<String,Object> header,@Path("page") int pageID);

    @POST("Request/Detail")
    Call<ResponseObject> readDetailRequest(@HeaderMap Map<String,Object> header,@Body Map<String,Object> body);

    @POST("Request/Handle")
    Call<ResponseObject> handleRequest(@Body Map<String,Object> body);

    @Multipart
    @POST("File/UploadImage")
    Call<ResponseObject> uploadPicture(@HeaderMap Map<String,Object> header, @Part MultipartBody.Part picture);


}