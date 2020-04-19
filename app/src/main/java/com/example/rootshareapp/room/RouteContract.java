package com.example.rootshareapp.room;

import android.provider.BaseColumns;

public class RouteContract {

    public RouteContract(){}

    public static abstract class Routes implements BaseColumns {//BaseColumnsの実装で自動で_idがふられるようになる

        public static final String TABLE_NAME = "routes";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_TITLE = "title";
        public static final String COL_UID = "uid";

    }
}
