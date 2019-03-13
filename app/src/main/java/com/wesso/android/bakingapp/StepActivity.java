package com.wesso.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.wesso.android.bakingapp.data.Step;

public class StepActivity extends SingleFragmentActivity {

    private static final String EXTRA_STEP_URL = "com.wesso.android.bakingapp.step_url";

    @Override
    protected Fragment createFragment() {
        Step step = getIntent().getParcelableExtra(EXTRA_STEP_URL);
        return StepFragment.newInstance(step);
    }


    public static Intent newIntent(Context context, Step step) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(EXTRA_STEP_URL, step);

        return intent;
    }
}
