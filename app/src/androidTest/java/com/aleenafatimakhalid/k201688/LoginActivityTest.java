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
public class LoginActivityTest {

    private ActivityScenario<login2> activityScenario;

    @Before
    public void launchActivity() {
        // Launch the login2 activity before each test
        activityScenario = ActivityScenario.launch(login2.class);
    }

    @Test
    public void testLogin() {
        // Type text in the email field
        Espresso.onView(ViewMatchers.withId(R.id.email)).perform(ViewActions.typeText("example1@gmail.com"));

        // Type text in the password field
        Espresso.onView(ViewMatchers.withId(R.id.password)).perform(ViewActions.typeText("12345678"));

        // Close the soft keyboard (if it is open)
        Espresso.closeSoftKeyboard();

        // Click the login button
        Espresso.onView(ViewMatchers.withId(R.id.login)).perform(ViewActions.click());
    }
}
