package com.aleenafatimakhalid.k201688;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignupActivityTest {

    private ActivityScenario<registration4> activityScenario;

    @Before
    public void launchActivity() {
        // Launch the registration4 activity before each test
        activityScenario = ActivityScenario.launch(registration4.class);
    }

    @Test
    public void testSignupFields() {
        // Type text in the name field
        Espresso.onView(withId(R.id.name)).perform(ViewActions.typeText("John Doe"));

        // Type text in the email field
        Espresso.onView(withId(R.id.email)).perform(ViewActions.typeText("johndoe@example.com"));

        // Type text in the password field
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText("password123"));

        // Type text in the number field
        Espresso.onView(withId(R.id.number)).perform(ViewActions.typeText("1234567890"));

    }
}
