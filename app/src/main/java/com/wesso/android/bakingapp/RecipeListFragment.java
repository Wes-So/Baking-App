package com.wesso.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeLoader;
import com.wesso.android.bakingapp.data.RecipeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recipe_recycler_view) RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private static final String TAG="RecipeListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list,container, false);
        ButterKnife.bind(this,view);
        boolean isPhone = getResources().getBoolean(R.bool.is_phone);
        if(isPhone) {
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            int numberOfColumns = 4;
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        }


        populateData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateData();
    }


    private void populateData() {
        if(mAdapter == null) {
            List<Recipe> recipes = new ArrayList<>();
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
            new RecipeAsyncTask().execute();
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateWidget(String recipeName, String ingredients){
        Log.d(TAG, "updateWidget");
        Context context = getActivity();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(Objects.requireNonNull(context).getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidget.class);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setTextViewText(R.id.widget_ingredients, ingredients);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mRecipeNameTextView;
        private Recipe mRecipe;

        RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe,parent,false));
            mRecipeNameTextView = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        void bind(Recipe recipe){
            mRecipeNameTextView.setText(recipe.getName());
            mRecipe = recipe;
        }

        @Override
        public void onClick(View view) {
            Intent intent = RecipeActivity.newIntent(getActivity(), mRecipe);
            startActivity(intent);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private  List<Recipe> mRecipes;

        RecipeAdapter(List<Recipe> recipes) {
            mRecipes = recipes;
        }


        @NonNull
        @Override
        public RecipeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeHolder recipeHolder, int position) {
            Recipe recipe = mRecipes.get(position);
            recipeHolder.bind(recipe);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        public void setRecipes(List<Recipe> mRecipes) {
            this.mRecipes = mRecipes;
        }
    }

    private class RecipeAsyncTask extends AsyncTask<Void, String, List<Recipe>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Recipe> doInBackground(Void... params) {
            RecipeLoader loader = new RecipeLoader();
            List<Recipe> recipes = loader.getRecipes();
            return recipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> result) {
            super.onPostExecute(result);
            mAdapter.setRecipes(result);
            mAdapter.notifyDataSetChanged();

        }
    }

}

