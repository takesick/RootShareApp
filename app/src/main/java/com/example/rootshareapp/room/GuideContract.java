package com.example.rootshareapp.room;

import android.provider.BaseColumns;

public class GuideContract {

    public GuideContract(){}

    public static abstract class Guide implements BaseColumns {//BaseColumnsの実装で自動で_idがふられるようになる

        public static final String TABLE_NAME = "guide";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_SPOT_NAME = "spot_name";
        public static final String COL_BODY = "body";
        public static final String COL_ROUTE_ID = "route_id";

    }
}
