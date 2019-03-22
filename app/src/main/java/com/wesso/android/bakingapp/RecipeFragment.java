package com.wesso.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeRepository;
import com.wesso.android.bakingapp.data.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {

    private static final String EXTRA_RECIPE_ID = "com.wesso.android.bakingapp.recipe_id";
    public static final String TAG = "RecipeFragment";
    private Recipe recipe;
    private RecipeFragment.StepAdapter mAdapter;
    private Callbacks mCallbacks;

    @BindView(R.id.steps_recycler_view) RecyclerView mStepRecyclerView;
    @BindView(R.id.recipe_name) TextView mNameTextView;
    @BindView(R.id.ingredients) TextView mIngredientsTextView;


    public interface Callbacks{
        void onStepSelected(Step step, ArrayList<Step> steps);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int recipeId = getArguments().getInt(EXTRA_RECIPE_ID);
        RecipeRepository repository = RecipeRepository.get(getActivity());
        recipe = repository.getRecipe(recipeId);
        Log.d(TAG, "Recipe Name: " + recipe.getName());
    }

    private void updateWidget(String recipeName, String ingredients){
        Context context = getActivity();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
        ComponentName thisWidget = new ComponentName(context, BakingAppWidget.class);
        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setTextViewText(R.id.widget_ingredients, ingredients);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: inflating the view");
        View view  = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this,view);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mNameTextView.setText(recipe.getName());
        mIngredientsTextView.setText(Utils.constructIngredients(recipe.getIngredients()));
        updateWidget(recipe.getName(), Utils.constructIngredients(recipe.getIngredients()));

        populateStepsData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        populateStepsData();
    }


    private void populateStepsData() {
        if(mAdapter == null) {
            List<Step> steps = recipe.getSteps();
            Log.d(TAG, "populateData: " + steps.size());
            mAdapter = new RecipeFragment.StepAdapter(steps);
            mStepRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    public static RecipeFragment newInstance(int recipeId){
        Bundle args = new Bundle();
        args.putInt(EXTRA_RECIPE_ID,recipeId);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mStepTextView;
        private Step mStep;
        private ArrayList<Step> mSteps;

        public StepHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_step,parent,false));
            mStepTextView = itemView.findViewById(R.id.recipe_step);
            itemView.setOnClickListener(this);
        }

        public void bind(Step step, List<Step> steps){
            mStepTextView.setText(step.getShortDescription());
            mStep = step;
            mSteps = new ArrayList<Step>(steps);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onStepSelected(mStep, mSteps);
        }
    }

    private class StepAdapter extends RecyclerView.Adapter<RecipeFragment.StepHolder> {

        private List<Step> mSteps;

        public StepAdapter(List<Step> steps) {
            mSteps = steps;
        }


        @NonNull
        @Override
        public RecipeFragment.StepHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new RecipeFragment.StepHolder(layoutInflater,viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecipeFragment.StepHolder stepHolder, int position) {
            Step step = mSteps.get(position);
            stepHolder.bind(step, mSteps);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }
    }

}
