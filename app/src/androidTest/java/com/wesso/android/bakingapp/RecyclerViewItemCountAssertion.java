package com.wesso.android.bakingapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.core.Is.is;

public class RecyclerViewItemCountAssertion implements ViewAssertion {


    private final Matcher<Integer> matcher;

    public RecyclerViewItemCountAssertion(int expectedCount) {
        this.matcher = is(expectedCount);
    }

    public RecyclerViewItemCountAssertion(Matcher<Integer> matcher) {
        this.matcher = matcher;
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        assertThat(adapter.getItemCount(), matcher);
    }
}
