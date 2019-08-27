package com.hensongeodata.technologies.etransit.controller;

import com.hensongeodata.technologies.etransit.controller.response.LatLonResponse;
import com.hensongeodata.technologies.etransit.controller.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

      @GET("csurvey/auth-user")
      Call<UserResponse> userLogin(
              @Query("u_mail") String u_email,
              @Query("pass") String u_password
      );

//      @POST("login")
//      Call<UserResponse> userLogin(
//              @Field("email") String u_email,
//              @Field("password") String u_password
//      );

      @GET("check-ea")
      Call<LatLonResponse> latlong(
              @Query("Longitude") String longitude,
              @Query("Latitude") String latitude,
              @Query("ea") String ea
      );

      @Headers({
              "Accept: application/json",
              "Content-Type: application/json"
      })
//      @POST(".")
//      Call<SendDataResponse> postRawJSON(
//              @Body JSONObject jsonObject
//      );
      @GET("recover-password")
      Call<UserResponse> pswChange(
              @Query("u_mail") String u_email
      );

}
