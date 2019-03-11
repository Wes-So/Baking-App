package com.wesso.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeRepository;

import java.util.List;

public class RecipeListFragment extends Fragment {

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private static final String TAG="RecipeListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list,container, false);
        mRecipeRecyclerView = view.findViewById(R.id.recipe_recycler_view);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
            RecipeRepository repository = RecipeRepository.get(getActivity());
            List<Recipe> recipes = repository.getRecipes();
            Log.d(TAG, "populateData: " + recipes.size());
            mAdapter = new RecipeAdapter(recipes);
            mRecipeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mRecipeNameTextView;
        private Recipe mRecipe;

        public RecipeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_recipe,parent,false));
            mRecipeNameTextView = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        public void bind(Recipe recipe){
            mRecipeNameTextView.setText(recipe.getName());
            mRecipe = recipe;
        }

        @Override
        public void onClick(View view) {
            Intent intent = RecipeActivity.newIntent(getActivity(), mRecipe.getId());
            startActivity(intent);
        }
    }

    private class RecipeAdapter extends RecyclerView.Adapter<RecipeHolder> {

        private List<Recipe> mRecipes;

        public RecipeAdapter(List<Recipe> recipes) {
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
    }

}

