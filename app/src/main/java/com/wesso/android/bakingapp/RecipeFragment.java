package com.wesso.android.bakingapp;

import android.appwidget.AppWidgetManager;
import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.wesso.android.bakingapp.data.Ingredient;
import com.wesso.android.bakingapp.data.Recipe;
import com.wesso.android.bakingapp.data.Step;
import com.wesso.android.bakingapp.db.IngredientData;
import com.wesso.android.bakingapp.db.IngredientDatabase;
import com.wesso.android.bakingapp.db.IngredientRepository;
import com.wesso.android.bakingapp.utils.Utils;
import com.wesso.android.bakingapp.widget.BakingAppWidgetProvider;
import com.wesso.android.bakingapp.widget.BakingAppWidgetService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeFragment extends Fragment {

    private static final String EXTRA_RECIPE = "com.wesso.android.bakingapp.recipe";
    private static final String TAG = "RecipeFragment";
    private Recipe recipe;
    private RecipeFragment.StepAdapter mAdapter;
    private Callbacks mCallbacks;

    @BindView(R.id.steps_recycler_view) RecyclerView mStepRecyclerView;
    @BindView(R.id.recipe_name) TextView mNameTextView;
    @BindView(R.id.ingredients) TextView mIngredientsTextView;
    @BindView(R.id.recipe_ll) LinearLayout mRecipe_LL;


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
        recipe = Objects.requireNonNull(getArguments()).getParcelable(EXTRA_RECIPE);
        Log.d(TAG, "Recipe Name: " + recipe.getName());
    }






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_recipe, container, false);

        ButterKnife.bind(this,view);

        mStepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mStepRecyclerView.setFocusable(false);
        mRecipe_LL.requestFocus();
        mNameTextView.setText(recipe.getName());
        mIngredientsTextView.setText(Utils.constructIngredients(recipe.getIngredients()));

        BakingAppWidgetService.startActionUpdateRecipe(getActivity(),recipe);
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

            mAdapter = new RecipeFragment.StepAdapter(steps);
            mStepRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

    }

    public static RecipeFragment newInstance(Recipe recipe){
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_RECIPE,recipe);

        RecipeFragment fragment = new RecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private class StepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mStepTextView;
        private Step mStep;
        private ArrayList<Step> mSteps;

        StepHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_step,parent,false));
            mStepTextView = itemView.findViewById(R.id.recipe_step);
            itemView.setOnClickListener(this);
        }

        void bind(Step step, List<Step> steps){
            mStepTextView.setText(step.getShortDescription());
            mStep = step;
            mSteps = new ArrayList<>(steps);
        }

        @Override
        public void onClick(View view) {
            mCallbacks.onStepSelected(mStep, mSteps);
        }
    }

    private class StepAdapter extends RecyclerView.Adapter<RecipeFragment.StepHolder> {

        private final List<Step> mSteps;

        StepAdapter(List<Step> steps) {
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
