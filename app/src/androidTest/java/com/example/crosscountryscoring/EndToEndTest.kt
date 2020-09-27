package com.example.crosscountryscoring

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import org.hamcrest.core.StringContains
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EndToEndTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

    companion object {
        @BeforeClass
        fun clearDatabase() {
            InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand("pm clear com.example.crosscountryscoring").close()
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
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.crosscountryscoring", appContext.packageName)
    }

    @Test
    fun raceFinisherCount_RestoredAfterOnCreate() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        activityScenario.scenario.recreate()
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
    }

    @Test
    fun teamScore_RestoredAfterOnCreate() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        activityScenario.scenario.recreate()
        // Assert Team score restored
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 1"))
                )
            )
        )
    }

    // FIXME after ActivityScenario.recreate(), databinding to currentRunnerTextView freezes. This
    //  is only the case while in instrumented test. Can't figure out why, so for now going to
    //  indirectly test via team score.
    @Test
    fun teamFinisherCount_RestoredAfterOnCreate() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        activityScenario.scenario.recreate()
        // Make sure Team # of finishers restored
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
        // If Team didn't know the pre-recreate # of finishers, it would increase score past 15
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 15"))
                )
            )
        )
    }

    @Test
    fun bindings_RestoredAfterScreenRotation() {
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        val device = UiDevice.getInstance(getInstrumentation())
        device.setOrientationRight()
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 3"))
            )
        )
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = targetContext.resources.getString(R.string.team_score, "[Team Name]", 3)
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
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 3"))
                )
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name] Score: 3"))
                )
            )
        )
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
    fun changeTeamName_StaysInRace() {
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("Elkhart Central Score: 0"))
                )
            )
        )
    }

    @Test
    fun endRace_ClearsScores() {
        // Start the race
        onView(withId(R.id.startRaceButton)).perform(click())
        // Increment a couple scores
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                click()
            ))
        // Pause and end race.
        // Espresso can't find overflow menu items by id
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        onView(withText(R.string.end_race)).perform(click())
        onView(withText(R.string.confirm_race_end)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        // Verify scores are cleared
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 1"))
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name] Score: 0"))
                )
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 0"))
                )
            )
        )
    }

    @Test
    fun resetRace_ClearsScores() {
        // Increment a couple scores
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                click()
            ))
        // Reset race
        // Espresso can't find overflow menu items by id
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        // Test will fail if reset race button isn't visible for some reason
        onView(withText(R.string.reset_race)).perform(click())
        onView(withText(R.string.confirm_race_reset)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        // Verify scores are cleared
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 1"))
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name] Score: 0"))
                )
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 0"))
                )
            )
        )
    }

    @Test
    fun cancelEndRace_DoesNotClearScores() {
        // Start the race
        onView(withId(R.id.startRaceButton)).perform(click())
        // Increment a couple scores
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                click()
            ))
        onView(withId(R.id.race_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                click()
            ))
        // Pause and end race.
        // Espresso can't find overflow items by id
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(R.string.end_race)).perform(click())
        onView(withText(R.string.confirm_race_end)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button2)).perform(click())
        // Verify scores are cleared
        onView(withId(R.id.currentRunnerTextView)).check(
            matches(
                withText(StringContains.containsString("Current Finisher: 3"))
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name] Score: 1"))
                )
            )
        )
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name] Score: 2"))
                )
            )
        )
    }

    @Test
    fun deleteTeamsScreen_DisplaysCorrectTeams() {
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        onView(withId(R.id.deleteTeamsButton)).perform(click())
        onView(withId(R.id.delete_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("Elkhart Central"))
                )
            )
        )
        onView(withId(R.id.delete_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
    }

    @Test
    fun deleteTeam_DisappearsFromAllScreens() {
        // Edit team name so we can verify that the correct team was deleted
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        // Go delete the team
        onView(withId(R.id.deleteTeamsButton)).perform(click())
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.confirmDeleteTeamsButton)).perform(click())
        onView(withText(R.string.confirm_delete_teams)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        // Verify team is missing from edit teams fragment
        onView(withId(R.id.edit_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
        // Verify team is missing from race fragment
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = targetContext.resources.getString(R.string.team_score, "[Team Name]", 0)
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
    fun cancelDeleteTeam_DoesNotDeleteTeam() {
        // Go "delete" the team
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.deleteTeamsButton)).perform(click())
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.confirmDeleteTeamsButton)).perform(click())
        onView(withText(R.string.confirm_delete_teams)).check(matches(isDisplayed()))
        // Cancel Deletion
        onView(withId(android.R.id.button2)).perform(click())
        // Verify teams still present on edit teams fragment
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        onView(withId(R.id.edit_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
        onView(withId(R.id.edit_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
    }

    @Test
    fun cancelDeleteTeamButton_DoesNotDeleteTeam() {
        // Go "delete" the team
        onView(withId(R.id.edit_race_button)).perform(click())
        onView(withId(R.id.deleteTeamsButton)).perform(click())
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.cancelDeleteTeamsButton)).perform(click())
        // Verify teams still present on edit teams fragment
        onView(withId(R.id.edit_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
        onView(withId(R.id.edit_teams_recycler_view)).check(
            matches(
                Utils.atPosition(
                    1,
                    hasDescendant(withText("[Team Name]"))
                )
            )
        )
    }

    @Test
    fun startRaceEndRace_ManipulatesTimer() {
        // Make sure timer is initialized correctly
        onView(withId(R.id.raceTimerTextView)).check(
            matches(
                withText(R.string.default_time)
            )
        )
        // Start the race
        onView(withId(R.id.startRaceButton)).perform(click())
        // Wait slightly more than 1 second so timer can iterate.
        // Normally would be wary of using Thread.sleep(), but this is one of the rare cases
        //  where it makes sense (because we are testing the behavior of a timer itself)
        Thread.sleep(1250)
        onView(withId(R.id.raceTimerTextView)).check(
            matches(
                withText("00:01")
            )
        )
        //Pause and end race.
        // Espresso can't find overflow menu items by id
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext);
        onView(withText(R.string.end_race)).perform(click())
        onView(withText(R.string.confirm_race_end)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.raceTimerTextView)).check(
            matches(
                withText("00:00")
            )
        )
    }

    @Test
    fun largeNumberOfTeams_EditNamesCorrectly() {
        onView(withId(R.id.edit_race_button)).perform(click())
        // Add lots of teams
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        // Change names so we can correctly identify them
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                12, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central2")))
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                15, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central3")))
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        // Verify team names are correct on race fragment
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText("Elkhart Central Score: 0"))
                )
            )
        )
        onView(withId(R.id.race_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(12))
            .check(matches(Utils.atPosition(12,
                hasDescendant(withText("Elkhart Central2 Score: 0")))))
        onView(withId(R.id.race_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(15))
            .check(matches(Utils.atPosition(15,
                hasDescendant(withText("Elkhart Central3 Score: 0")))))
    }

    @Test
    fun largeNumberOfTeams_DeletesCorrectly() {
        onView(withId(R.id.edit_race_button)).perform(click())
        // Add lots of teams
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        onView(withId(R.id.add_team_button)).perform(click())
        // Change names so we can correctly identify them
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                12, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central2")))
        onView(withId(R.id.edit_teams_recycler_view))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(
                15, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central3")))
        // Delete teams
        onView(withId(R.id.deleteTeamsButton)).perform(click())
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(12,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.delete_teams_recycler_view)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(15,
                Utils.clickChildViewWithId(R.id.deleteTeamCheckbox)
            ))
        onView(withId(R.id.confirmDeleteTeamsButton)).perform(click())
        onView(withText(R.string.confirm_delete_teams)).check(matches(isDisplayed()))
        onView(withId(android.R.id.button1)).perform(click())
        // Verify teams missing from race fragment
        // Assert correct number of teams
        onView(withId(R.id.edit_teams_recycler_view)).check(RecyclerViewItemCountAssertion(19))
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        val targetContext: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val scoreString = targetContext.resources.getString(R.string.team_score, "[Team Name]", 0)
        onView(withId(R.id.race_recycler_view)).check(
            matches(
                Utils.atPosition(
                    0,
                    hasDescendant(withText(scoreString))
                )
            )
        )
        // Check positions above in case deleting position 0 worked but not the others
        onView(withId(R.id.race_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(12))
            .check(matches(Utils.atPosition(11,
                hasDescendant(withText(scoreString)))))
        onView(withId(R.id.race_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(12))
            .check(matches(Utils.atPosition(12,
                hasDescendant(withText(scoreString)))))
        onView(withId(R.id.race_recycler_view))
            .perform(scrollToPosition<RecyclerView.ViewHolder>(15))
            .check(matches(Utils.atPosition(15,
                hasDescendant(withText(scoreString)))))
    }
}