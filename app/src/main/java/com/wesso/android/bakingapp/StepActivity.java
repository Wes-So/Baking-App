package com.wesso.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import com.wesso.android.bakingapp.data.Step;
import java.util.ArrayList;
import java.util.List;


public class StepActivity extends SingleFragmentActivity {

    private static final String EXTRA_STEP_URL = "com.wesso.android.bakingapp.step_url";
    private static final String EXTRA_STEPS = "com.wesso.android.bakingapp.steps";
    private static final String TAG = "Step Activity";
    private ArrayList<Step> mSteps;

    @Override
    protected Fragment createFragment() {
        Step step = getIntent().getParcelableExtra(EXTRA_STEP_URL);
        mSteps = getIntent().getParcelableArrayListExtra(EXTRA_STEPS);
        return StepFragment.newInstance(step, mSteps);
    }

    public static Intent newIntent(Context context, Step step, ArrayList<Step> steps) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(EXTRA_STEP_URL, step);
        intent.putParcelableArrayListExtra(EXTRA_STEPS, steps);
        return intent;
    }
}
