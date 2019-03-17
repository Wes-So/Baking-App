package com.wesso.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.wesso.android.bakingapp.data.Step;

public class RecipeActivity extends SingleFragmentActivity
    implements RecipeFragment.Callbacks {

    private static final String EXTRA_RECIPE_ID = "com.wesso.android.bakingapp.recipe_id";

    @Override
    protected Fragment createFragment() {
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID,0);
        return RecipeFragment.newInstance(recipeId);
    }

    public static Intent newIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }


    @Override
    public void onStepSelected(Step step) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = StepActivity.newIntent(this, step);
            startActivity(intent);
        } else {
            Fragment newDetail = StepFragment.newInstance(step);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }
}
