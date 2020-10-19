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
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditTeamsTests {

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
    fun changeTeamName_StaysInRace() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")
                )
            )
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
        // Verify
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = context.resources.getString(
            R.string.team_score,
            "Elkhart Central",
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
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    @Test
    fun largeNumberOfTeams_EditNamesCorrectly() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        // Add lots of teams
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        // Change names so we can correctly identify them
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")
                )
            )
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    12, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central2")
                )
            )
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    15, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central3")
                )
            )
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
        // Verify team names are correct on race fragment
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        var scoreString = context.resources.getString(
            R.string.team_score,
            "Elkhart Central",
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
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )

        scoreString = context.resources.getString(
            R.string.team_score,
            "Elkhart Central2",
            0,
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0)
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(12))
            .check(
                ViewAssertions.matches(
                    Utils.atPosition(
                        12,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                    )
                )
            )

        scoreString = context.resources.getString(
            R.string.team_score,
            "Elkhart Central3",
            0,
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0),
            NullConverter.runnerPlaceToString(context, 0)
        )
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(15))
            .check(
                ViewAssertions.matches(
                    Utils.atPosition(
                        15,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                    )
                )
            )
    }
}