package com.wesso.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.wesso.android.bakingapp.R;
import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.db.IngredientData;
import com.wesso.android.bakingapp.db.IngredientDatabase;
import com.wesso.android.bakingapp.db.IngredientRepository;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "BakingAppWidgetProvider";
    public final static String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";
    private  static final String  DATABASE_NAME = "ingredient_database";
    private IngredientDatabase ingredientDatabase;

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        Log.d(TAG, "updateAppWidget: " + recipe.getName());
//        dbOperations(recipe);
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_collection_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());

        setRemoteViewAdapter(context,views,recipe);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    private static void setRemoteViewAdapter(Context context, @NonNull final RemoteViews views, Recipe recipe) {
        Intent intent = new Intent(context, BakingAppListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);
    }

    //Database operations
//    private static void dbOperations(Context context, Recipe recipe) {
//
//        IngredientRepository repository = new IngredientRepository(this);
//
//        //delete all records
//        clearTable(repository);
//
//        //insert ingredients
//        insertIngredients(repository,recipe.getIngredients());
//
//        //validate records
//        validateRecords(repository);
//
//    }

//    private static void validateRecords(IngredientRepository repository) {
//        try{
//            List<IngredientData> dataList = repository.getAllIngredients();
//            Log.d(TAG, "validateRecords: " + dataList.size());
//        } catch (Exception e) {
//            Log.e(TAG, "validateRecords: ",e );
//        }
//
//
//    }
//
//    private static void clearTable(IngredientRepository repository) {
//        Log.d(TAG, "clearing table");
//        repository.deleteAll();
//    }
//
//    private static void insertIngredients(IngredientRepository repository, List<Ingredient> ingredientList) {
//        Log.d(TAG, "dbOperations: size of ingredients " + ingredientList.size());
//        for(Ingredient ingredient: ingredientList) {
//            IngredientData data = new IngredientData(ingredient.getQuantity(),ingredient.getMeasure(),ingredient.getIngredient());
//            repository.insert(data);
//        }
//    }


    // Widgets allow click handlers to only launch pending intents
//        Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
//        serviceIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPE_WIDGET);
//        Intent intent = new Intent(context, RecipeListActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);


}

