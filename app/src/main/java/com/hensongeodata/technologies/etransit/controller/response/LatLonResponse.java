package com.hensongeodata.technologies.etransit.controller.response;

import com.google.gson.annotations.SerializedName;

public class LatLonResponse {
      @SerializedName("status")
      private Boolean longlatError;
      @SerializedName("msg")
      private String longlatMessage;

      public Boolean getLonglatError() {
            return longlatError;
      }

      public void setLonglatError(Boolean longlatError) {
            this.longlatError = longlatError;
      }

      public String getLonglatMessage() {
            return longlatMessage;
      }

      public void setLonglatMessage(String longlatMessage) {
            this.longlatMessage = longlatMessage;
      }
}
