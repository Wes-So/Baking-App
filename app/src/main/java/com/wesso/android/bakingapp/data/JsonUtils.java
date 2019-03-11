package com.wesso.android.bakingapp.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

//    public static final String KEY_NAME = "name";
//    public static final String KEY_MAIN_NAME = "mainName";
//    public static final String KEY_ALSO_KNOW_AS = "alsoKnownAs";
//    public static final String KEY_PLACE_OF_ORIGIN = "placeOfOrigin";
//    public static final String KEY_DESCRIPTION = "description";
//    public static final String KEY_IMAGE = "image";
//    public static final String KEY_INGREDIENTS = "ingredients";
//
//    public static Sandwich parseSandwichJson(String json) {
//
//        Sandwich sandwich = null;
//
//
//        try {
//            JSONObject json_obj = new JSONObject(json);
//            JSONObject nameArr = json_obj.getJSONObject(KEY_NAME);
//            String mainName = nameArr.getString(KEY_MAIN_NAME);
//            JSONArray alsoKnownAs = nameArr.getJSONArray(KEY_ALSO_KNOW_AS);
//            List<String> alsoKnownAsList = convertJsonArrayToList(alsoKnownAs);
//            String placeOfOrigin = json_obj.getString(KEY_PLACE_OF_ORIGIN);
//            String description = json_obj.getString(KEY_DESCRIPTION);
//            String image = json_obj.getString(KEY_IMAGE);
//            JSONArray ingredients = json_obj.getJSONArray(KEY_INGREDIENTS);
//            List<String> ingredientsList = convertJsonArrayToList(ingredients);
//
//
//            sandwich = new Sandwich(mainName, alsoKnownAsList, placeOfOrigin,description,image,ingredientsList);
//        } catch(JSONException je) {
//            je.printStackTrace();
//        } finally {
//            return sandwich;
//        }
//    }
//
//    private static List<String> convertJsonArrayToList(JSONArray jArr) throws JSONException{
//        List<String> arrList = new ArrayList<>();
//        for (int i=0;i<jArr.length();i++){
//            arrList.add(jArr.getString(i));
//        }
//
//
//
//        return arrList;
//
//    }
}