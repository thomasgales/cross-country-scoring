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
class DeleteTeamsTests {

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
    fun cancelDeleteTeam_DoesNotDeleteTeam() {
        // Go "delete" the team
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.deleteTeamsButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.confirmDeleteTeamsButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_delete_teams))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Cancel Deletion
        Espresso.onView(ViewMatchers.withId(android.R.id.button2)).perform(ViewActions.click())
        // Verify teams still present on edit teams fragment
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
    }

    @Test
    fun cancelDeleteTeamButton_DoesNotDeleteTeam() {
        // Go "delete" the team
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.deleteTeamsButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.cancelDeleteTeamsButton))
            .perform(ViewActions.click())
        // Verify teams still present on edit teams fragment
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
    }

    @Test
    fun deleteTeam_DisappearsFromAllScreens() {
        // Edit team name so we can verify that the correct team was deleted
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")
                )
            )
        // Go delete the team
        Espresso.onView(ViewMatchers.withId(R.id.deleteTeamsButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.confirmDeleteTeamsButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_delete_teams))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        // Verify team is missing from edit teams fragment
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
        // Verify team is missing from race fragment
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
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
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
    }

    @Test
    fun deleteTeamsScreen_DisplaysCorrectTeams() {
        Espresso.onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")
                )
            )
        Espresso.onView(ViewMatchers.withId(R.id.deleteTeamsButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Elkhart Central"))
                )
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name]"))
                )
            )
        )
    }


    @Test
    fun largeNumberOfTeams_DeletesCorrectly() {
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
        // Delete teams
        Espresso.onView(ViewMatchers.withId(R.id.deleteTeamsButton)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                12,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.delete_teams_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                15,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            )
        )
        Espresso.onView(ViewMatchers.withId(R.id.confirmDeleteTeamsButton))
            .perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.confirm_delete_teams))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(android.R.id.button1)).perform(ViewActions.click())
        // Verify teams missing from race fragment
        // Assert correct number of teams
        Espresso.onView(ViewMatchers.withId(R.id.edit_teams_recycler_view)).check(RecyclerViewItemCountAssertion(19))
        Espresso.onView(ViewMatchers.withContentDescription(R.string.nav_app_bar_navigate_up_description))
            .perform(ViewActions.click())
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
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                )
            )
        )
        // Check positions above in case deleting position 0 worked but not the others
        Espresso.onView(ViewMatchers.withId(R.id.race_recycler_view))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(12))
            .check(
                ViewAssertions.matches(
                    Utils.atPosition(
                        11,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(scoreString))
                    )
                )
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