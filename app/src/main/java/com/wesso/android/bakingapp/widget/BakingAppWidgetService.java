package com.wesso.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wesso.android.bakingapp.data.Recipe;

public class BakingAppWidgetService extends IntentService {


    public final static String ACTION_UPDATE_RECIPE_WIDGET = "com.wesso.android.bakingapp.recipwidget";
    public final static String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";
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

        Log.d(TAG, "handleActionUpdateWidgets: ");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidgetProvider.class));
        BakingAppWidgetProvider.updateAllWidgets(this,appWidgetManager,appWidgetIds,recipe);
    }
}
