package com.wesso.android.bakingapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface IngredientDataDao {
        @Insert
        void insert(IngredientData ingredient);

        @Query("DELETE FROM ingredient_data_table")
        void deleteAll();

        @Query("SELECT * from ingredient_data_table ORDER BY id ASC")
        List<IngredientData> getAllIngredients();
}






