package com.example.crosscountryscoring

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.hamcrest.core.StringContains
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PersistenceTests {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @BeforeClass
        fun clearDatabase() {
            InstrumentationRegistry.getInstrumentation()
                .uiAutomation
                .executeShellCommand("pm clear com.example.crosscountryscoring")
                .close()
        }
    }

    /**
     * Adds two teams to the race with the default team name.
     */
    @Before
    fun addTeams() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
    }


    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.crosscountryscoring", appContext.packageName)
    }

    @Test
    fun raceFinisherCount_RestoredAfterOnCreate() {
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        activityScenario.scenario.recreate()
        Espresso.onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
    }

    @Test
    fun teamScore_RestoredAfterOnCreate() {
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        activityScenario.scenario.recreate()
        // Assert Team score restored
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            1,
            NullConverter.runnerPlaceToString(context, 1),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0)
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    // FIXME after ActivityScenario.recreate(), databinding to currentRunnerTextView freezes. This
    //  is only the case while in instrumented test. Can't figure out why, so for now going to
    //  indirectly test via team score.
    @Test
    fun teamFinisherCount_RestoredAfterOnCreate() {
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        activityScenario.scenario.recreate()
        // Make sure Team # of finishers restored
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            15,
            NullConverter.runnerPlaceToString(context, 1),
            NullConverter.runnerPlaceToString(context, 2),
            NullConverter.runnerPlaceToString(context, 3),
            NullConverter.runnerPlaceToString(context, 4),
            NullConverter.runnerPlaceToString(context, 5),
            NullConverter.runnerPlaceToString(context, 6),
            NullConverter.runnerPlaceToString(context, 7)
        )
        // If Team didn't know the pre-recreate # of finishers, it would increase score past 15
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    @Test
    fun bindings_RestoredAfterScreenRotation() {
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        val device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        device.setOrientationRight()
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 3"))
            )
        )
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            3,
            NullConverter.runnerPlaceToString(context, 1),
            NullConverter.runnerPlaceToString(context, 2),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0)
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }
}