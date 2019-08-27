package com.hensongeodata.technologies.etransit.model;

public class LatLon {

      public String lon;
      public String lat;
      public String alt;
      public String accuracy;

      public LatLon(String lon, String lat, String alt, String accuracy) {
            this.lon = lon;
            this.lat = lat;
            this.alt = alt;
            this.accuracy = accuracy;
      }

      public LatLon() {
      }

      public String getLon() {
            return lon;
      }

      public void setLon(String lon) {
            this.lon = lon;
      }

      public String getLat() {
            return lat;
      }

      public void setLat(String lat) {
            this.lat = lat;
      }

      public String getAlt() {
            return alt;
      }

      public void setAlt(String alt) {
            this.alt = alt;
      }

      public String getAccuracy() {
            return accuracy;
      }

      public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
      }
}
