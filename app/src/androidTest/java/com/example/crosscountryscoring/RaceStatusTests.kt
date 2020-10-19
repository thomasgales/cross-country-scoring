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
import org.hamcrest.core.StringContains
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RaceStatusTests {

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
    fun cancelEndRace_DoesNotClearScores() {
        // Start the race
        Espresso.onView(ViewMatchers.withId(R.id.startRaceButton)).perform(ViewActions.click())
        // Increment a couple scores
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )
        // Pause and end race.
        // Espresso can't find overflow items by id
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        Espresso.onView(ViewMatchers.withText(R.string.end_race)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_race_end))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button2)).perform(ViewActions.click())
        // Verify scores are not cleared
        Espresso.onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 3"))
            )
        )
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        var scoreString = context.resources.getString(
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
        scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            2,
            NullConverter.runnerPlaceToString(context, 2),
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
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    @Test
    fun endRace_DoesNotClearScores() {
        // Start the race
        Espresso.onView(ViewMatchers.withId(R.id.startRaceButton)).perform(ViewActions.click())
        // Increment a couple scores
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )
        // Pause and end race.
        // Espresso can't find overflow menu items by id
        Espresso.openActionBarOverflowOrOptionsMenu(
            InstrumentationRegistry.getInstrumentation().targetContext
        )
        Espresso.onView(ViewMatchers.withText(R.string.end_race)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_race_end))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        // Verify scores are cleared
        Espresso.onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 3"))
            )
        )
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        var scoreString = context.resources.getString(
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
        scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            2,
            NullConverter.runnerPlaceToString(context, 2),
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
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    @Test
    fun resetRace_ClearsScores() {
        // Increment a couple scores
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )
        // Reset race
        // Espresso can't find overflow menu items by id
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        // Test will fail if reset race button isn't visible for some reason
        Espresso.onView(ViewMatchers.withText(R.string.reset_race)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_race_reset))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        // Verify scores are cleared
        Espresso.onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 1"))
            )
        )
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            0,
            NullConverter.runnerPlaceToString(context, 0),
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
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
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


    @Test
    fun startRaceEndRace_ManipulatesTimer() {
        // Make sure timer is initialized correctly
        Espresso.onView(ViewMatchers.withId(R.id.raceTimerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(R.string.default_time)
            )
        )
        // Start the race
        Espresso.onView(ViewMatchers.withId(R.id.startRaceButton)).perform(ViewActions.click())
        // Wait slightly more than 1 second so timer can iterate.
        // Normally would be wary of using Thread.sleep(), but this is one of the rare cases
        //  where it makes sense (because we are testing the behavior of a timer itself)
        Thread.sleep(1250)
        Espresso.onView(ViewMatchers.withId(R.id.raceTimerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText("00:01")
            )
        )
        // Pause and end race.
        // Espresso can't find overflow menu items by id
        Espresso.openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext);
        Espresso.onView(ViewMatchers.withText(R.string.end_race)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_race_end))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.raceTimerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(R.string.default_time)
            )
        )
    }
}