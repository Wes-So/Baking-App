package com.wesso.android.bakingapp.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class IngredientContract {


    public static final String AUTHORITY = "com.wesso.android.bakingapp";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    private static final String PATH_INGREDIENT = "ingredients";
    public static final class IngredientEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENT).build();

        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_INGREDIENT = "ingredient";
    }

}
