package com.example.rootshareapp.model;

import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Public_Location {

        public static final String FIELD_DESTINATION = "destination";
        public static final String FIELD_TITLE = "tag";
        public static final String FIELD_LATITUDE = "latitude";
        public static final String FIELD_LONGITUDE = "longitude";
        public static final String FIELD_ACCURACY = "accuracy";
        public static final String FIELD_CREATE_AT = "created_at";
        public static final String FIELD_UID = "uid";

        public String tag;
        public double latitude;
        public double longitude;
        public double accuracy;
        public String created_at;
        public String uid;
        public String comment;

        public Public_Location() {}

        public Public_Location(String tag, double latitude, double longitude, double accuracy, String created_at, String comment) {
            this.tag = tag;
            this.latitude = latitude;
            this.longitude = longitude;
            this.accuracy = accuracy;
            this.created_at = created_at;
            this.comment = comment;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double Longitude) {
            this.longitude = longitude;
        }

        public double getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(double accuracy) {
            this.accuracy = accuracy;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
}
