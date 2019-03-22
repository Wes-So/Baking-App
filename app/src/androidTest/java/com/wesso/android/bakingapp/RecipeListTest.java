package com.wesso.android.bakingapp;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;

@RunWith(AndroidJUnit4.class)
public class RecipeListTest  {

    private static final String BAKED_ITEM = "Green Tea";
    private static final String TAG = "RecipeListTest";

    @Before
    public void launchActivity() {
        ActivityScenario.launch(RecipeListActivity.class);
    }

    @Test
    public void itemCountExactTest() {
        onView(withId(R.id.recipe_recycler_view)).check(new RecyclerViewItemCountAssertion(4));
    }


    @Test
    public void itemCountGreaterThanTest() {
        onView(withId(R.id.recipe_recycler_view)).check(new RecyclerViewItemCountAssertion(greaterThan(3)));

    }

    @Test
    public void itemCountLessThanTest(){
        onView(withId(R.id.recipe_recycler_view)).check(new RecyclerViewItemCountAssertion(lessThan(5)));
    }



}


//    @Before
//    public void launchActivity() {
//        ActivityScenario.launch(RecipeListActivity.class);
//    }
//    @Test
//    public void clickViewItem_RecipeListActivity() {
//
//        onView(ViewMatchers.withId(R.id.recipe_recycler_view))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
//
//        String itemElementText = getApplicationContext().getResources().getString(
//                R.string.recipe_name);
//
//        Log.d(TAG, "clickViewItem_RecipeListActivity: " + itemElementText);
//
////                + String.valueOf(0);
////        onView(withText(itemElementText)).check(matches(isDisplayed()));
//    }

