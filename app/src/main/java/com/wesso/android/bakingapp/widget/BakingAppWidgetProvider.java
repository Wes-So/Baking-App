package com.wesso.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.wesso.android.bakingapp.R;
import com.wesso.android.bakingapp.data.Recipe;

public class BakingAppWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "BakingAppWidgetProvider";


    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Recipe recipe) {
        Log.d(TAG, "updateAppWidget: " + recipe.getName());
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_collection_widget);
        views.setTextViewText(R.id.widget_recipe_name, recipe.getName());

        setRemoteViewAdapter(context,views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipe);
        }
    }

    private static void setRemoteViewAdapter(Context context, @NonNull final RemoteViews views) {
        Intent intent = new Intent(context, BakingAppListWidgetService.class);
        views.setRemoteAdapter(R.id.widget_list, intent);
    }




    // Widgets allow click handlers to only launch pending intents
//        Intent serviceIntent = new Intent(context, BakingAppWidgetService.class);
//        serviceIntent.setAction(BakingAppWidgetService.ACTION_UPDATE_RECIPE_WIDGET);
//        Intent intent = new Intent(context, RecipeListActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);


}

