package com.example.rootshareapp.sqlite;

import android.provider.BaseColumns;

public final class LocationContract {

    public LocationContract(){}

    public static abstract class Locations implements BaseColumns {//BaseColumnsの実装で自動で_idがふられるようになる

        public static final String TABLE_NAME = "locations";
        public static final String COL_LATITUDE = "latitude";
        public static final String COL_LONGITUDE = "longitude";
        public static final String COL_ACCURACY = "accuracy";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UID = "uid";
    }

}
