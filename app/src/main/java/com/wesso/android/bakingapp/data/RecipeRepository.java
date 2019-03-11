package com.wesso.android.bakingapp.data;

import android.content.Context;
import java.util.List;

public class RecipeRepository {

    private static RecipeRepository sRecipeRepository;
    private List<Recipe> mRecipes;


    public static RecipeRepository get(Context context) {
        if(sRecipeRepository == null) {
            sRecipeRepository = new RecipeRepository(context);
         }
        return sRecipeRepository;
    }

    private RecipeRepository(Context context) {
        mRecipes = RecipeLoader.getRecipes(context);
    }

    public List<Recipe> getRecipes() {
        return mRecipes;
    }

    public Recipe getRecipe(int id){
        for(Recipe recipe: mRecipes){
            if(recipe.getId() == id){
                return recipe;
            }
        }
        return null;
    }
}
