package com.wesso.android.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeRepository;

import java.util.List;

public class RecipeFragment extends Fragment {

    private static final String EXTRA_RECIPE_ID = "com.wesso.android.bakingapp.recipe_id";
    public static final String TAG = "RecipeFragment";
    private Recipe recipe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        RecipeRepository repository = RecipeRepository.get(getActivity());
        recipe = repository.getRecipe(recipeId);
        Log.d(TAG, "Recipe Name: " + recipe.getName());

    }

    private void populateData(View view) {
        TextView mIngredientsTextView = view.findViewById(R.id.ingredients);
        TextView mNameTextView = view.findViewById(R.id.recipe_name);

        mNameTextView.setText(recipe.getName());
        mIngredientsTextView.setText(constructIngredients(recipe.getIngredients()));
    }

    private String constructIngredients(List<Ingredient> ingredients){
        StringBuffer result = new StringBuffer();
        for(Ingredient ingredient : ingredients) {
            result.append("\u2022 " + ingredient.getIngredient() + " : " + ingredient.getQuantity() + " " + ingredient.getMeasure() + "\n");
        }

        return result.toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_recipe, container, false);
        populateData(view);
        return view;
    }

    public static RecipeFragment newInstance(int recipeId){
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID,recipeId);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }


}
