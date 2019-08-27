package com.hensongeodata.technologies.etransit.controller.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.hensongeodata.technologies.etransit.MyApplication;
import com.hensongeodata.technologies.etransit.controller.Api;
import com.hensongeodata.technologies.etransit.controller.BroadcastCall;
import com.hensongeodata.technologies.etransit.controller.response.LatLonResponse;
import com.hensongeodata.technologies.etransit.controller.response.UserResponse;
import com.hensongeodata.technologies.etransit.model.Keys;
import com.hensongeodata.technologies.etransit.model.user.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkRequestUtil {

      private String TAG = NetworkRequestUtil.class.getSimpleName();
      private Context context;
      public ProgressDialog dialog;
      MyApplication myApplication;

      public NetworkRequestUtil(Context context) {
            this.context = context;
      }

      @NonNull
      public static RequestBody createPartFromString(String descriptionString) {
            return RequestBody.create(
                    okhttp3.MultipartBody.FORM, descriptionString);
      }

      public void loginRequest(String email, String password){

            myApplication = new MyApplication();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Logging In...");
            dialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Keys.CENTRAL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Api api = retrofit.create(Api.class);
            Call<UserResponse> call = api.userLogin(email, password);

            call.enqueue(new Callback<UserResponse>() {
                  @Override
                  public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                              // Check login response for error
                              if (!response.body().getError()){

                                    Log.e(TAG, "onResponse: " + response.raw() );

                                    BroadcastCall.publishSignin(context, Keys.STATUS_SUCCESS, "email");
                                    SharedPrefManager.getInstance(context).userLogin(response.body().getUser());
//                                    myApplication.toastShortMessage(context, "Success");
                              }
                        } else {
                              // Handle other response codes
                              Log.e(TAG, "onResponse: " + response.raw() );
                              BroadcastCall.publishSignin(context, Keys.STATUS_FAIL, "Error at server side");
                        }
                  }

                  @Override
                  public void onFailure(Call<UserResponse> call, Throwable t) {
                        dialog.dismiss();
                        Log.e(TAG, "onFailure: " + t.getMessage() );
//                        myApplication.toastShortMessage(context, t.getMessage());
                        BroadcastCall.publishSignin(context, Keys.STATUS_FAIL, t.getMessage());
                  }
            });

      }

      public void sendLocscast( String ea, String longitude, String latitude){

            myApplication = new MyApplication();
            dialog = new ProgressDialog(context);
            dialog.setMessage("Checking Location...");
            dialog.show();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Keys.EA_AREA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);
            Call<LatLonResponse> locCall = api.latlong(longitude, latitude, ea);

            locCall.enqueue(new Callback<LatLonResponse>() {
                  @Override
                  public void onResponse(Call<LatLonResponse> call, Response<LatLonResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){

                              if (response.body().getLonglatError()){
//                                    myApplication.toastShortMessage(context, "Sent");
                                    BroadcastCall.publishLocscast(context, Keys.STATUS_SUCCESS, "Sent Now");
                              }
                              else {
                                    String message = response.body().getLonglatMessage();
                                    BroadcastCall.publishLocscast(context, Keys.STATUS_FAIL, message);
                              }

                        }
                  }

                  @Override
                  public void onFailure(Call<LatLonResponse> call, Throwable t) {
                        dialog.dismiss();
//                        myApplication.toastShortMessage(context, t.getMessage());
                        BroadcastCall.publishLocscast(context, Keys.STATUS_FAIL, t.getMessage());
                  }
            });
      }

//      public void sendData(final JSONObject jsonObject){
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl(Keys.BASE_SEND)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            Api api = retrofit.create(Api.class);
//            Call<SendDataResponse> call = api.postRawJSON(jsonObject);
//            call.enqueue(new Callback<SendDataResponse>() {
//                  @Override
//                  public void onResponse(Call<SendDataResponse> call, Response<SendDataResponse> response) {
//
//                        if (response.isSuccessful()){
//                              if (response.body().getStatus().toString().equalsIgnoreCase("success")){
////                                    Toast.makeText(context, "Sent All", Toast.LENGTH_SHORT).show();
//                                    JSONObject jObject = null;
//                                    try {
//                                          jObject = new JSONObject(String.valueOf(jsonObject));
//                                          if (jObject != null){
//
//                                                if (jObject.has("sub_id")){
//                                                      String subid = jObject.getString("sub_id");
////                                                      Toast.makeText(context, "Data " + subid, Toast.LENGTH_SHORT).show();
//                                                      BroadcastCall.publishSentAnswer(context, Keys.STATUS_SUCCESS, subid);
//                                                }
//                                                else {
////                                                      myApplication.toastLongMessage(context, "Has No Form Name");
//                                                      BroadcastCall.publishSentAnswer(context, Keys.STATUS_FAIL, "Failed");
//                                                }
//
//                                          }
//
//                                    } catch (JSONException e) {
//                                          e.printStackTrace();
//                                    }
//                              }
//                              else{
////                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//                                    BroadcastCall.publishSentAnswer(context, Keys.STATUS_FAIL, "Error");
//                              }
//                        }
//                  }
//
//                  @Override
//                  public void onFailure(Call<SendDataResponse> call, Throwable t) {
////                        Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                        BroadcastCall.publishSentAnswer(context, Keys.STATUS_FAIL, t.getMessage());
//                  }
//            });
//
//      }

      public void requestPassword(String u_mail){
            dialog = new ProgressDialog(context);
            dialog.setMessage("Requesting Password Reset...");
            dialog.setInverseBackgroundForced(true);
            dialog.show();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Keys.PWD_CHANGE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            Api api = retrofit.create(Api.class);
            Call<UserResponse> call =api.pswChange(u_mail);
            call.enqueue(new Callback<UserResponse>() {
                  @Override
                  public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()){
                              if (!response.body().getError()){
                                    String message = response.body().getMessage();
                                    BroadcastCall.publishChangePWD(context, Keys.STATUS_SUCCESS, message);
                              }
                              else
                                    BroadcastCall.publishChangePWD(context, Keys.STATUS_FAIL, response.body().getMessage());
                        }
                        else
                              BroadcastCall.publishChangePWD(context, Keys.STATUS_FAIL, "System Error");
                  }

                  @Override
                  public void onFailure(Call<UserResponse> call, Throwable t) {
                        dialog.dismiss();
                        BroadcastCall.publishChangePWD(context, Keys.STATUS_FAIL, "Could not connect. \n Please check your Internet Connection");
                  }
            });
      }

}
