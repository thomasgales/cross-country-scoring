package com.example.crosscountryscoring

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
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
        onView(ViewMatchers.withId(R.id.return_to_race_button))
            .perform(ViewActions.click())
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("com.example.crosscountryscoring", appContext.packageName)
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
    fun currentFinisher_StopIncreasingAfter7() {
        val startingFinisher =
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
        onView(ViewMatchers.withId(R.id.return_to_race_button)).perform(ViewActions.click())
        onView(ViewMatchers.withId(R.id.race_recycler_view)).check(
            ViewAssertions.matches(
                Utils.atPosition(
                    0,
                    ViewMatchers.hasDescendant(ViewMatchers.withText("Elkhart Central Score: 0"))
                )
            )
        )
    }

}