package com.hensongeodata.technologies.etransit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;

import com.hensongeodata.technologies.etransit.controller.BroadcastCall;
import com.hensongeodata.technologies.etransit.controller.request.NetworkRequestUtil;
import com.hensongeodata.technologies.etransit.service.LocationServiceRun;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {

      private static final String TAG = MainActivity.class.getSimpleName();

      TextView latText;
      private IntentFilter intentFilter;
      private MainActivityBroadCastReceiver receiver;
      MyApplication myApplication;
      NetworkRequestUtil networkRequestUtil;

      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            init();
      }

      private void init(){

            latText = findViewById(R.id.lat);

            checkPermission();
            myApplication = new MyApplication();
            networkRequestUtil = new NetworkRequestUtil(this);

            intentFilter = new IntentFilter(BroadcastCall.LOCSCAST_SERVICE);
            receiver = new MainActivityBroadCastReceiver();
            registerReceiver(receiver, intentFilter);

            startService(new Intent(getApplicationContext(), LocationServiceRun.class));

      }

      public void checkPermission(){

            // Requesting ACCESS_FINE_LOCATION using Dexter library
            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(new PermissionListener() {
                          @Override
                          public void onPermissionGranted(PermissionGrantedResponse response) {
                                Log.v(TAG, "onPermissionGranted: Permission is successfully granted");
                          }

                          @Override
                          public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()) {
                                      // open device settings when the permission is
                                      // denied permanently
                                      openSettings();
                                }
                          }

                          @Override
                          public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                          }
                    }).check();

      }

      private boolean checkPermissions() {
            int permissionState = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            return permissionState == PackageManager.PERMISSION_GRANTED;
      }

      private void openSettings() {

            checkPermissions();
            Intent intent = new Intent();
            intent.setAction(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package",
                    BuildConfig.APPLICATION_ID, null);
            intent.setData(uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
      }

      @Override
      protected void onStart() {
            super.onStart();
      }

      @Override
      protected void onDestroy() {
            super.onDestroy();
            unregisterReceiver(receiver);
      }

      @Override
      public void onBackPressed() {
            super.onBackPressed();
            unregisterReceiver(receiver);
      }

      @Override
      protected void onResume() {
            super.onResume();
            registerReceiver(receiver, intentFilter);
      }

      class MainActivityBroadCastReceiver extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {

                  String lat_message = intent.getStringExtra("latitude");
                  String long_message = intent.getStringExtra("longitude");

                  latText.setText("" + lat_message + ", " + "" + long_message);

            }
      }

}
