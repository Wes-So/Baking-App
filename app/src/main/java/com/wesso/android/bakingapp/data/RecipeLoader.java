package com.wesso.android.bakingapp.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class RecipeLoader {

    private final static String TAG = "RecipeLoader";

    private static Recipe getRecipe(JSONObject jsonRecipe) throws JSONException {
        return new Recipe(
                jsonRecipe.optInt("id"),
                jsonRecipe.optString("name"),
                getIngredients(jsonRecipe.optJSONArray("ingredients")),
                getListOfSteps(jsonRecipe.optJSONArray("steps")),
                jsonRecipe.optInt("servings"),
                jsonRecipe.optString("image")
        );
    }

    public static List<Recipe>  getRecipes(Context context){
        List<Recipe> recipes = new ArrayList<>();
        try {
            String jsonString = getJSONString(context);
            Log.d(TAG, "getRecipes: " + jsonString);
            JSONArray jsonArray = new JSONArray(jsonString);

            for(int i = 0; i < jsonArray.length(); i++) {
                recipes.add(getRecipe(jsonArray.getJSONObject(i)));
            }
        } catch(JSONException jsone){
            Log.d(TAG, "Error converting string to JSON String");
            jsone.printStackTrace();
        } catch(IOException ioe) {
            Log.d(TAG, "Error getting JSON String from file");
            ioe.printStackTrace();
        }

        return recipes;
    }

    private static List<Ingredient> getIngredients(JSONArray jsonIngredients) throws JSONException {
        List<Ingredient> ingredients = new ArrayList<>();
        for(int i = 0; i<jsonIngredients.length(); i++) {
            ingredients.add(getIngredient(jsonIngredients.getJSONObject(i)));
        }

        return ingredients;
    }


    private static Ingredient getIngredient(JSONObject jsonIngredient){
        return new Ingredient(
                jsonIngredient.optInt("quantity"),
                jsonIngredient.optString("measure"),
                jsonIngredient.optString("ingredient")
        );
    }

    private static List<Step> getListOfSteps(JSONArray jsonStepsArray) throws JSONException{
        List<Step> listOfSteps = new ArrayList<>();
        for(int i = 0; i<jsonStepsArray.length(); i++){
            listOfSteps.add(getStep(jsonStepsArray.getJSONObject(i)));
        }

        return listOfSteps;
    }

    private static Step getStep(JSONObject jsonStep){
        return new Step(jsonStep.optInt("id"),
                jsonStep.optString("shortDescription"),
                jsonStep.optString("description"),
                jsonStep.optString("videoURL"),
                jsonStep.optString("thumbnailURL"));
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String getJSONString(Context context) throws IOException {
        String jsonString;
        InputStream is = context.getAssets().open("recipes.json");

        byte[] buffer = new byte[is.available()];
        is.read(buffer);
        is.close();

        jsonString = new String(buffer, StandardCharsets.UTF_8);

        return jsonString;
    }



}
