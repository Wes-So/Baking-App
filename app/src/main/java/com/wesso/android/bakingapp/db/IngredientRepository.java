package com.wesso.android.bakingapp.db;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class IngredientRepository {

    private IngredientDataDao mDao;
    private List<IngredientData> ingredientDataList;

    public IngredientRepository(Application application){
        IngredientDatabase db = IngredientDatabase.getDatabase(application);
        mDao = db.dao();
    }

    public List<IngredientData> getAllIngredients() throws Exception{
        ingredientDataList = new queryAllAsyncTask(mDao).execute().get();
        return ingredientDataList;
    }

    public void insert(IngredientData ingredientData) {
        new insertAsyncTask(mDao).execute(ingredientData);

    }

    public void deleteAll() {
        new deleteAsyncTask(mDao).execute();
    }


    private static class insertAsyncTask extends AsyncTask<IngredientData, Void, Void> {

        private IngredientDataDao mAsyncTaskDao;

        insertAsyncTask(IngredientDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final IngredientData... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {
        private IngredientDataDao mAsyncTaskDao;

        deleteAsyncTask(IngredientDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }


    private static class queryAllAsyncTask extends AsyncTask<Void, Void, List<IngredientData>> {
        private IngredientDataDao mAsyncTaskDao;

        queryAllAsyncTask(IngredientDataDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<IngredientData> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAllIngredients();
        }
    }



}
