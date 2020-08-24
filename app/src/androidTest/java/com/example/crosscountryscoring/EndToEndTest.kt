package com.example.crosscountryscoring

import android.app.PendingIntent.getActivity
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.hamcrest.core.StringContains
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EndToEndTest {

    @get:Rule
    val activityScenario = ActivityScenarioRule(MainActivity::class.java)

//    @get:Rule
//    var activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

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
        onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.add_team_button)).perform(ViewActions.click())
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
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        activityScenario.scenario.recreate()
        onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
    }

    @Test
    fun teamScore_RestoredAfterOnCreate() {
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        activityScenario.scenario.recreate()
        // Assert Team score restored
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 1"))
                )
            )
        )
    }

    // FIXME after ActivityScenario.recreate(), databinding to currentRunnerTextView freezes. This
    //  is only the case while in instrumented test. Can't figure out why, so for now going to
    //  indirectly test via team score.
    @Test
    fun teamFinisherCount_RestoredAfterOnCreate() {
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        activityScenario.scenario.recreate()
        // Make sure Team # of finishers restored
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        // If Team didn't know the pre-recreate # of finishers, it would increase score past 15
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 15"))
                )
            )
        )
    }

    @Test
    fun teamButtons_IterateFinisherCount() {
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 2"))
            )
        )
    }

    @Test
    fun teamButtons_IncreaseScore() {
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 3"))
                )
            )
        )
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 3"))
                )
            )
        )
    }

    @Test
    fun currentFinisher_StopsIncreasingAfter7() {
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 8"))
            )
        )
    }

    @Test
    fun changeTeamName_StaysInRace() {
        onView(ViewMatchers.withId(R.id.edit_race_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.edit_teams_recycler_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0, Utils.replaceTextForId(R.id.team_name_edit_box, "Elkhart Central")))
        onView(withContentDescription(R.string.nav_app_bar_navigate_up_description)).perform(click())
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Elkhart Central Score: 0"))
                )
            )
        )
    }

    @Test
    fun endRace_ClearsScores() {
        // Start the race
        onView(ViewMatchers.withId(R.id.toggleRaceStatusButton)).perform(click())
        // Increment a couple scores
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                ViewActions.click()
            ))
        onView(ViewMatchers.withId(R.id.race_recycler_view)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,
                ViewActions.click()
            ))
        // Pause and end race
        onView(ViewMatchers.withId(R.id.toggleRaceStatusButton)).perform(click())
        onView(ViewMatchers.withId(R.id.endRaceButton)).perform(click())
        // Verify scores are cleared
        onView(ViewMatchers.withId(R.id.currentRunnerTextView)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(StringContains.containsString("Current Finisher: 1"))
            )
        )
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    1,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 0"))
                )
            )
        )
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("[Team Name] Score: 0"))
                )
            )
        )
    }

}