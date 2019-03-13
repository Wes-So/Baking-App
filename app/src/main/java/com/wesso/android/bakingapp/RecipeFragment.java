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

import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.RecipeRepository;
import com.wesso.android.bakingapp.data.Step;

import java.util.List;

public class RecipeFragment extends Fragment {

    private static final String EXTRA_RECIPE_ID = "com.wesso.android.bakingapp.recipe_id";
    public static final String TAG = "RecipeFragment";
    private RecyclerView mStepRecyclerView;
    private RecipeFragment.StepAdapter mAdapter;
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
        mStepRecyclerView = view.findViewById(R.id.steps_recycler_view);
        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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

        public StepHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_step,parent,false));
            mStepTextView = itemView.findViewById(R.id.recipe_step);
            itemView.setOnClickListener(this);
        }

        public void bind(Step step){
            mStepTextView.setText(step.getShortDescription());
            mStep = step;
        }

        @Override
        public void onClick(View view) {
            if(mStep.getVideoURL().isEmpty()){
                Toast toast = Toast.makeText(getActivity(), "No video for this step",Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            Intent intent = StepActivity.newIntent(getActivity(), mStep);
            startActivity(intent);
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
            stepHolder.bind(step);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }
    }

}
