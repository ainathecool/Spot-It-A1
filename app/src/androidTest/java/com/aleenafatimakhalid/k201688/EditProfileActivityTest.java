package com.aleenafatimakhalid.k201688;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditProfileActivityTest {

    private ActivityScenario<editprofile14> activityScenario;

    @Before
    public void launchActivity() {
        // Launch the editprofile14 activity before each test
        activityScenario = ActivityScenario.launch(editprofile14.class);
    }

    @Test
    public void testSaveProfile() {
        // Type text in the name field
        Espresso.onView(ViewMatchers.withId(R.id.name)).perform(ViewActions.typeText("John Doe"));

        // Type text in the email field
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("johndoe@example.com"));

        // Type text in the number field
        Espresso.onView(ViewMatchers.withId(R.id.number)).perform(ViewActions.typeText("1234567890"));

        // Close the soft keyboard (if it is open)
        Espresso.closeSoftKeyboard();

        // Click the save changes button
        Espresso.onView(ViewMatchers.withId(R.id.saveChanges)).perform(ViewActions.click());

    }
}
