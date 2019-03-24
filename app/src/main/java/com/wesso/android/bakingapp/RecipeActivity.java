package com.wesso.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.Step;

import java.util.ArrayList;

public class RecipeActivity extends SingleFragmentActivity
    implements RecipeFragment.Callbacks {

    private static final String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";

    @Override
    protected Fragment createFragment() {
        Recipe recipe = getIntent().getParcelableExtra(EXTRA_RECIPE);
        return RecipeFragment.newInstance(recipe);
    }

    public static Intent newIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        Log.d("TEST", "newIntent: " + recipe.getIngredients().size());
        intent.putExtra(EXTRA_RECIPE, recipe);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


    @Override
    public void onStepSelected(Step step, ArrayList<Step> steps) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = StepActivity.newIntent(this, step, steps);
            startActivity(intent);
        } else {
            Fragment newDetail = StepFragment.newInstance(step, steps);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }


}
