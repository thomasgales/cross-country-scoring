package com.example.crosscountryscoring

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.example.crosscountryscoring.NullConverter.runnerPlaceToString
import org.hamcrest.core.StringContains
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScoringTests {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @BeforeClass
        fun clearDatabase() {
            getInstrumentation()
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
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
    }

    @Test
    fun currentFinisher_StopsIncreasingAfter7() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 8"))
            )
        )
    }

    @Test
    fun teamButtons_IncreaseScore() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                click()
            ))
        // Verify team scores and runner finishing positions correct
        val context: Context = getInstrumentation().targetContext
        var scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            3,
            runnerPlaceToString(context, 1),
            runnerPlaceToString(context, 2),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0)
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText(scoreString))
                )
            )
        )
        scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            3,
            runnerPlaceToString(context, 3),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0)
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText(scoreString))
                )
            )
        )
    }

    @Test
    fun teamButtons_IterateFinisherCount() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
    }

    // Used to have bug where TeamViewModels were getting replaced every time the user navigated
    //  away from the EditTeams or DeleteTeams fragment. Prevent regression! If a problem, undo
    //  finisher will expose it.
    @Test
    fun teamViewModel_Persists() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(click())
        onView(withId(R.id.undo_finisher_button)).perform(click())
        // Verify the 2nd finisher is no longer counted
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
        val context: Context = getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            1,
            runnerPlaceToString(context, 1),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0)
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText(scoreString))
                )
            )
        )
    }

    @Test
    fun undoFinisher_ReversesScoring() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.undo_finisher_button)).perform(click())
        // Verify the 2nd finisher is no longer counted
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
        val context: Context = getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "[Team Name]",
            1,
            runnerPlaceToString(context, 1),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0),
            runnerPlaceToString(context, 0)
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText(scoreString))
                )
            )
        )
    }
}