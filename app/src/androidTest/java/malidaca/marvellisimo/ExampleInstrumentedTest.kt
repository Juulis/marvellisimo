package malidaca.marvellisimo

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import malidaca.marvellisimo.activities.CharacterListActivity
import malidaca.marvellisimo.activities.LoginActivity
import malidaca.marvellisimo.activities.MenuActivity
import org.junit.*

import org.junit.runner.RunWith

import org.junit.Assert.*
import android.support.test.InstrumentationRegistry.getTargetContext
import android.content.ComponentName
import android.support.design.widget.Snackbar
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.rule.IntentsTestRule
import android.support.test.espresso.matcher.ViewMatchers.*
import malidaca.marvellisimo.activities.SeriesActivity
import malidaca.marvellisimo.utilities.LoadDialog
import org.junit.Rule


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class TestLogin {

    @Rule
    @JvmField
    val rule = IntentsTestRule(LoginActivity::class.java)

    @Test
    fun loginFail() {
        Log.e("@Test", "Performing login Fail test")

        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("test@test.com"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_password)).perform(ViewActions.typeText("password"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_login_button)).perform(ViewActions.click())

        Thread.sleep(1000) //wait for snackbar

        onView(withId(android.support.design.R.id.snackbar_text))
                .check(matches(withText(R.string.signin_failed_wrong_credentials)))
    }

    @Test
    fun loginSuccess() {

        Log.e("@Test", "Performing login success test")

        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("test@test.com"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_password)).perform(ViewActions.typeText("qwerty"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_login_button)).perform(ViewActions.click())


        Thread.sleep(3000) //wait for the timeout and activity to change on slow emulators

        intended(hasComponent(ComponentName(getTargetContext(), MenuActivity::class.java)))
    }



}
@RunWith(AndroidJUnit4::class)
class TestApp {

    @Rule
    @JvmField
    val rule = IntentsTestRule(LoginActivity::class.java)

    @Before
    fun login(){
        Espresso.onView(withId(R.id.login_email)).perform(ViewActions.typeText("test@test.com"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_password)).perform(ViewActions.typeText("qwerty"))
        Espresso.closeSoftKeyboard()
        Espresso.onView(withId(R.id.login_login_button)).perform(ViewActions.click())
        Thread.sleep(3000)
    }

    @Test
    fun characterList() {
        Espresso.onView(withId(R.id.menu_button_characters))
                .perform(ViewActions.click())
        Thread.sleep(5000)
        intended(hasComponent(ComponentName(getTargetContext(), CharacterListActivity::class.java)))
    }

    @Test
    fun seriesList() {
        Espresso.onView(withId(R.id.menu_button_series))
                .perform(ViewActions.click())
        Thread.sleep(5000)
        intended(hasComponent(ComponentName(getTargetContext(), SeriesActivity::class.java)))
    }

}


