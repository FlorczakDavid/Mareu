package com.florczakdavid.maru;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;


import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static com.florczakdavid.maru.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.florczakdavid.maru.utils.Utils.getText;

import com.florczakdavid.maru.ui.AddMeetingActivity;
import com.florczakdavid.maru.ui.FilterMeetingFragment;
import com.florczakdavid.maru.ui.MainActivity;
import com.florczakdavid.maru.utils.DeleteViewAction;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    // This is fixed
    private static int ITEMS_COUNT = 3;

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);


    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * We ensure that our recyclerview is displaying at least on item
     */
    @Test
    public void myMeetingsList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 2
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(ITEMS_COUNT-1));
    }

    @Test
    public void addMeetingFloatingActionButton_tapAction_shouldShowAddMeetingActivity() {
        Intents.init();
        onView(allOf(withId(R.id.addMeetingFloatingActionButton), isDisplayed())).perform(click());

        intended(hasComponent(AddMeetingActivity.class.getName()));
    }

    @Test
    public void addMeeting_shouldAddItem() {
        Intents.init();
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(ITEMS_COUNT));
        onView(allOf(withId(R.id.addMeetingFloatingActionButton), isDisplayed())).perform(click());


        onView(allOf(withId(R.id.newMeetingTopicTextView), isDisplayed())).perform(replaceText("TOPIC"));
        onView(allOf(withId(R.id.newMeetingParticipantsTextView), isDisplayed())).perform(replaceText("NAME"));

        onView(allOf(withId(R.id.newMeetingSaveActionMaterialButton), isDisplayed())).perform(click());

        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(ITEMS_COUNT+1));
        // Attempt to scroll to an item that contains the special text.
        onView(ViewMatchers.withId(R.id.meetingListRecyclerView))
                // scrollTo will fail the test if no item matches.
                .perform(RecyclerViewActions.scrollTo(
                        hasDescendant(withText("TOPIC"))
                ));
    }

    /*
    * Tester Ajout Reu
    * changement filtre
    * stop filtering
    * */

//    @Test
//    public void filterImageButton_tapAction_shouldShowFilterMeetingFragment() {
//        Intents.init();
//        onView(allOf(withId(R.id.filterImageButton), isDisplayed())).perform(click());
//        onView(withId(R.id.fragmentFilterMeetingsTitleTextView)).matches(isDisplayed());
//        onView(allOf(withId(R.id.fragmentFilterMeetingByTimeButton), isDisplayed())).perform(click());
//    }
}