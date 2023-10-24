package com.aleenafatimakhalid.k201688;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ItemPostActivityTest {

    private ActivityScenario<itempost13> activityScenario;

    @Before
    public void launchActivity() {
        // Launch the itempost13 activity before each test
        activityScenario = ActivityScenario.launch(itempost13.class);
    }

    @Test
    public void testItemPost() {
        // Enter a name
        Espresso.onView(ViewMatchers.withId(R.id.entername))
                .perform(ViewActions.typeText("Sample Item"));

        // Enter hourly rate
        Espresso.onView(ViewMatchers.withId(R.id.enterhourly))
                .perform(ViewActions.typeText("10"));

        // Enter description
        Espresso.onView(ViewMatchers.withId(R.id.enterdescription))
                .perform(ViewActions.typeText("This is a sample item description"));

        // Enter city
        Espresso.onView(ViewMatchers.withId(R.id.bestmatch))
                .perform(ViewActions.typeText("Sample City"));

        // Perform a click action on the "Post Item" button
        Espresso.onView(ViewMatchers.withId(R.id.post_item_btn))
                .perform(ViewActions.click());
  }
}
