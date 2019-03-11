package com.wesso.android.bakingapp;

import android.support.v4.app.Fragment;

public class RecipeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RecipeListFragment();
    }
}
