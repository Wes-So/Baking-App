package com.wesso.android.bakingapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {IngredientData.class}, version = 1, exportSchema = false)
public abstract class IngredientDatabase extends RoomDatabase {

    public abstract IngredientDataDao dao();

    private static volatile IngredientDatabase INSTANCE;

    static IngredientDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (IngredientDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), IngredientDatabase.class, "ingredient_database")
                        .build();
            }

        }
        return INSTANCE;
    }
}
