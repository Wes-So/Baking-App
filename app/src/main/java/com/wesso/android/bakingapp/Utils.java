package com.wesso.android.bakingapp;

import com.wesso.android.bakingapp.data.Ingredient;

import java.util.List;

public class Utils {
    public static  String constructIngredients(List<Ingredient> ingredients){
        StringBuffer result = new StringBuffer();
        for(Ingredient ingredient : ingredients) {
            result.append("\u2022 " + ingredient.getIngredient() + " : " + ingredient.getQuantity() + " " + ingredient.getMeasure() + "\n");
        }

        return result.toString();
    }
}
