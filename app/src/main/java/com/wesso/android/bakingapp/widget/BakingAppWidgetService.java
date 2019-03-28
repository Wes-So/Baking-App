package com.wesso.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.provider.IngredientContract;

import java.util.List;


public class BakingAppWidgetService extends IntentService {


    private final static String ACTION_UPDATE_RECIPE_WIDGET = "com.wesso.android.bakingapp.recipewidget";
    private final static String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";
    private final static String TAG = "BakingAppWidgetService";

    public BakingAppWidgetService(){
        super("BakingAppService");
    }


    public static void startActionUpdateRecipe(Context context, Recipe recipe) {
        Log.d(TAG, "startActionUpdateRecipe: Updating widget");
        Intent intent = new Intent(context, BakingAppWidgetService.class);
        intent.setAction(ACTION_UPDATE_RECIPE_WIDGET);
        intent.putExtra(EXTRA_RECIPE, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_RECIPE_WIDGET.equals(action)) {
                Recipe recipe = intent.getParcelableExtra(EXTRA_RECIPE);
                Log.d(TAG, "onHandleIntent: " + recipe.getName());
                handleActionUpdateWidgets(recipe);
            }
        }
    }

    private void handleActionUpdateWidgets(Recipe recipe) {

        Log.d(TAG, "Deleting table");
        deleteDB();

        Log.d(TAG, "Insert to db");
        insertDB(recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateAllWidgets(this,appWidgetManager,appWidgetIds,recipe);
    }

    private void deleteDB(){
        getContentResolver().delete(IngredientContract.IngredientEntry.CONTENT_URI,null,null);
    }

    private void insertDB(Recipe recipe){
        List<Ingredient> ingredientList = recipe.getIngredients();
        Log.d(TAG, "insertDB: " + ingredientList.size());
        for(Ingredient ingredient : ingredientList) {
            ContentValues values = new ContentValues();
            values.put(IngredientContract.IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
            values.put(IngredientContract.IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
            values.put(IngredientContract.IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());

            getContentResolver().insert(IngredientContract.IngredientEntry.CONTENT_URI, values);
            Log.d(TAG, "insert: " + ingredient.getIngredient());
        }


    }
}
