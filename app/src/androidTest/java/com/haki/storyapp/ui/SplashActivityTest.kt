package com.haki.storyapp.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.platform.app.InstrumentationRegistry
import com.haki.storyapp.R
import com.haki.storyapp.di.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SplashActivityTest {

    private val dummyEmail = "ahah@yahoo.com"
    private val dummyPass = "ssssssss"

    @get:Rule
    val activityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Before
    fun setUp() {
        Intents.init()
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        Intents.release()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun fromSplashUntilLogin() {

        //wait for splashscreen to finish
        Thread.sleep(SplashActivity.SPLASH_TIME)

        //check if already in WelcomeActivity
        Intents.intended(hasComponent(WelcomeActivity::class.java.name))

        //swipe in welcomeActivity
        onView(withId(R.id.motionLayout)).perform(swipeLeft(), swipeLeft())

        //check if login button is displayed
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))

        //click login button (WelcomeActivity)
        onView(withId(R.id.btn_login)).perform(click())

        //check if already in LoginActivity
        Intents.intended(hasComponent(LoginActivity::class.java.name))

        //fill the form
        onView(withId(R.id.et_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.et_pass)).perform(typeText(dummyPass), closeSoftKeyboard())

        //click login button (LoginActivity)
        onView(withId(R.id.btn_login)).perform(click())

        //check if already in MainActivity
        Intents.intended(hasComponent(MainActivity::class.java.name))

        //check if recyclerview is displayed
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }


    @Test
    fun fromMainUntilLogout() {
        Thread.sleep(SplashActivity.SPLASH_TIME)

        Intents.intended(hasComponent(MainActivity::class.java.name))

        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)

        onView(withText(R.string.log_out)).perform(click())
        Intents.intended(hasComponent(WelcomeActivity::class.java.name))
    }

}
