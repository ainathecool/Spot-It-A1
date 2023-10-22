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
        Espresso.onView(withId(R.id.email)).perform(ViewActions.typeText("example1@gmail.com"));

        // Type text in the password field
        Espresso.onView(withId(R.id.password)).perform(ViewActions.typeText("12345678"));

        // Click the login button
        Espresso.onView(withId(R.id.login)).perform(ViewActions.click());

    }
}
