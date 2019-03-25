package com.wesso.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.wesso.android.bakingapp.R;
import com.wesso.android.bakingapp.RecipeListActivity;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeRepository;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "BakingAppWidgetProvider";

     public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        Log.d(TAG, "updateAppWidget: " + recipe.getName());
        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe ){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,recipe);
        }
    }


    private RemoteViews getListRemoteView(){
         return null;
    }

    // Widgets allow click handlers to only launch pending intents
//        Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
//        serviceIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPE_WIDGET);
//        Intent intent = new Intent(context, RecipeListActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);


}

