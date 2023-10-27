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

        // Wait for 3 seconds (or your specified SPLASH_TIME) for Splash screen to finish
//        onView(withId(R.id.)).check(matches(isDisplayed()))
        Thread.sleep(SplashActivity.SPLASH_TIME)

        Intents.intended(hasComponent(WelcomeActivity::class.java.name))

        onView(withId(R.id.motionLayout)).perform(swipeLeft(), swipeLeft())

        onView(withId(R.id.btn_login)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_login)).perform(click())

        Intents.intended(hasComponent(LoginActivity::class.java.name))

        onView(withId(R.id.et_email)).perform(typeText(dummyEmail), closeSoftKeyboard())
        onView(withId(R.id.et_pass)).perform(typeText(dummyPass), closeSoftKeyboard())

        onView(withId(R.id.btn_login)).perform(click())

        Intents.intended(hasComponent(MainActivity::class.java.name))
        onView(withId(R.id.rv_story)).check(matches(isDisplayed()))
    }

    @Test
    fun uploadImageTest() {
        Thread.sleep(SplashActivity.SPLASH_TIME)

        Intents.intended(hasComponent(MainActivity::class.java.name))

        onView(withText(R.id.fab_add)).perform(click())
        Intents.intended(hasComponent(UploadActivity::class.java.name))


    }

//    @Test
//    fun fromMainUntilLogout() {
//        Thread.sleep(SplashActivity.SPLASH_TIME)
//
//        Intents.intended(hasComponent(MainActivity::class.java.name))
//
//        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
//
//        onView(withText(R.string.log_out)).perform(click())
//        Intents.intended(hasComponent(WelcomeActivity::class.java.name))
//    }

}
