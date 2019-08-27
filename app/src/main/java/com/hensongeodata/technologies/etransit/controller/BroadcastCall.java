package com.hensongeodata.technologies.etransit.controller;

import android.content.Context;
import android.content.Intent;

public class BroadcastCall {

      public static String SIGN_IN = "com.hensongeodata.broadcast.signin";
      public static String SIGN_UP = "com.hensongeodata.broadcast.signup";
      public static String CHANGE_PASSWORD = "com.hensongeodata.broadcast.change_password";
      public static String LOCSCAST = "com.hensongeodata.broadcast.locscast";
      public static String LOCSCAST_SERVICE = "com.hensongeodata.broadcast.locscast.service";
      public static String ANSWER_SAVED = "com.hensongeodata.broadcast.database.saved";
      public static String ANSWER_SENT = "com.hensongeodata.broadcast.database.sent";

      public static void publishSignup(Context context, int status, String message){
            Intent intent = new Intent(SIGN_UP);
            intent.setAction(SIGN_UP);
            intent.putExtra("message",message);
            intent.putExtra("status",status);
            context.sendBroadcast(intent);
      }
      public static void publishSignin(Context context,int status,String message){
            Intent intent = new Intent(SIGN_IN);
            intent.setAction(SIGN_IN);
            intent.putExtra("status",status);
            intent.putExtra("message", message);
            context.sendBroadcast(intent);
      }

      public static void publishChangePWD(Context context,int status,String message){
            Intent intent = new Intent(CHANGE_PASSWORD);
            intent.setAction(CHANGE_PASSWORD);
            intent.putExtra("status",status);
            intent.putExtra("message", message);
            context.sendBroadcast(intent);
      }

      public static void publishLocscast(Context context, int status, String message){
            Intent intent = new Intent(LOCSCAST);
            intent.setAction(LOCSCAST);
            intent.putExtra("message", message);
            intent.putExtra("status", status);
            context.sendBroadcast(intent);
      }

      public static void publishLocscastService(Context context, String lat, String lon){
            Intent intent = new Intent(LOCSCAST_SERVICE);
            intent.setAction(LOCSCAST_SERVICE);
            intent.putExtra("latitude", lat);
            intent.putExtra("longitude", lon);
            context.sendBroadcast(intent);
      }

      public static void publishSentAnswer(Context context, int status, String message){
            Intent intent = new Intent(ANSWER_SENT);
            intent.setAction(ANSWER_SENT);
            intent.putExtra("status",status);
            intent.putExtra("message", message);
            context.sendBroadcast(intent);
      }


}
