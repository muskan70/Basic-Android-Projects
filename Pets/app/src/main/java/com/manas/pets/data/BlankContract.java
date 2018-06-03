package com.manas.pets.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class BlankContract {
    public static final String CONTENT_AUTHORITY="com.manas.pets";
    public static final Uri BASE_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_PETS="pets";

    public static class BlankEntry implements BaseColumns{

        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_URI,PATH_PETS);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        public static final String TABLE_NAME="pets";
        public static final String COLUMN_PET_NAME="name";
        public static final String COLUMN_PET_BREED="breed";
        public static final String COLUMN_PET_GENDER="gender";
        public static final String COLUMN_PET_WEIGHT="weight";
        public static final String COLUMN_ID=BaseColumns._ID;

        public static final int GENDER_MALE=1;
        public static final int GENDER_FEMALE =2;
        public static final int GENDER_UNKNOWN=0;

        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }

    }

}
