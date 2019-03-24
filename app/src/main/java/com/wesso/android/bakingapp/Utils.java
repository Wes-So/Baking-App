package com.wesso.android.bakingapp;

import android.util.Log;

import com.wesso.android.bakingapp.data.Ingredient;

import java.util.List;

class Utils {

    public static  String constructIngredients(List<Ingredient> ingredients){
        StringBuilder result = new StringBuilder();
        for(Ingredient ingredient : ingredients) {

            result.append("\u2022 ").append(ingredient.getIngredient()).append(" : ").append(ingredient.getQuantity()).append(" ").append(ingredient.getMeasure()).append("\n");
        }

        return result.toString();
    }
}
