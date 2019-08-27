package com.hensongeodata.technologies.etransit.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hensongeodata.technologies.etransit.controller.BroadcastCall;

public class LocationServiceRun extends Service  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

      private static String TAG= LocationServiceRun.class.getSimpleName();
      private GoogleApiClient mGoogleApiClient;
      private LocationRequest mLocationRequest;
      private LocationCallback mLocationCallback;
      private FusedLocationProviderClient mFusedLocationProviderClient;

      public LocationServiceRun() {
      }

      protected void startLocationUpdate(){
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                  return;
            }
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);

      }

      private void setLocation(Location location){
            if (location != null){
                  String lat = String.valueOf(location.getLatitude()) ;
                  String lon = String.valueOf(location.getLongitude());

                  BroadcastCall.publishLocscastService(getApplicationContext(), lat, lon);

                  Log.v(TAG, "setLocation: " + lat + ", " + lon );

            }else
                  Log.d(TAG, "setLocation: Location is null");
      }

      private void checkForLocationEnabled() {
            Log.v(TAG, "Check enabled");
            showLocationDialog();
      }

      public void showLocationDialog() {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                            builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                  @Override
                  public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                        final Status status = locationSettingsResult.getStatus();
                        switch (status.getStatusCode()) {
                              case LocationSettingsStatusCodes.SUCCESS:
                                    Log.v(TAG, "Check enabled 0");
                                    // All location settings are satisfied. The client can
                                    // initialize location requests here.
                                    break;
                              case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                                    break;
                              case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                    break;
                        }
                  }
            });
      }

      @Override
      public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
           return null;
      }

      @Override
      public void onCreate() {
            super.onCreate();
            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

            if (mGoogleApiClient == null){
                  mGoogleApiClient = new GoogleApiClient.Builder(this)
                          .addConnectionCallbacks(this)
                          .addOnConnectionFailedListener(this)
                          .addApi(com.google.android.gms.location.LocationServices.API)
                          .build();
            }

            if (mLocationRequest == null){
                  mLocationRequest = new LocationRequest();
                  mLocationRequest.setInterval(15000);
                  mLocationRequest.setFastestInterval(10000);
                  mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }

            mLocationCallback = new LocationCallback(){
                  @Override
                  public void onLocationResult(LocationResult locationResult) {
                        if (locationResult == null)
                              return;
                        for (Location location : locationResult.getLocations()){
                              setLocation(location);
                        }
                  }
            };
      }

      @Override
      public int onStartCommand(Intent intent, int flags, int startId) {
            if (!mGoogleApiClient.isConnected()){
                  mGoogleApiClient.connect();
            }
            checkForLocationEnabled();
            return START_STICKY;
      }

      @Override
      public void onDestroy() {
            super.onDestroy();
      }

      @Override
      public void onLocationChanged(Location location) {
            setLocation(location);
      }

      @Override
      public void onConnected(@Nullable Bundle bundle) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                  return;
            }
            Location mLastLocation = com.google.android.gms.location.LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            setLocation(mLastLocation);
            startLocationUpdate();
            Log.v(TAG, "Connection Established");
      }

      @Override
      public void onConnectionSuspended(int i) {

      }

      @Override
      public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

      }
}
