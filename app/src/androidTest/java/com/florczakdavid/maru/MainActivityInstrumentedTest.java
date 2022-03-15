package com.florczakdavid.maru;

import static androidx.test.espresso.Espresso.getIdlingResources;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.release;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.CoordinatesProvider;
import androidx.test.espresso.action.GeneralClickAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.Tap;
import androidx.test.espresso.action.Tapper;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;


import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static com.florczakdavid.maru.utils.RecyclerViewItemCountAssertion.withItemCount;
import static com.florczakdavid.maru.utils.Utils.atPosition;
import static com.florczakdavid.maru.utils.Utils.getText;

import com.florczakdavid.maru.ui.AddMeetingActivity;
import com.florczakdavid.maru.ui.FilterMeetingFragment;
import com.florczakdavid.maru.ui.MainActivity;
import com.florczakdavid.maru.utils.DeleteViewAction;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import static org.hamcrest.core.StringContains.containsString;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    // This is fixed
    private int itemCount = 3;

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);


    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }


    @Test
    public void _7myMeetingsList_shouldNotBeEmpty() {
        // First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed()))
                .check(matches(hasMinimumChildCount(1)));
    }


    @Test
    public void _6myMeetingsList_deleteAction_shouldRemoveItem() {
        RecyclerView recyclerView = mActivityRule.getActivity().findViewById(R.id.meetingListRecyclerView);
        itemCount = recyclerView.getChildCount();

        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(itemCount));
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        itemCount -= 1;
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(itemCount));
    }

    @Test
    public void _5addMeetingFloatingActionButton_tapAction_shouldShowAddMeetingActivity() {
        Intents.init();
        onView(allOf(withId(R.id.addMeetingFloatingActionButton), isDisplayed())).perform(click());

        intended(hasComponent(AddMeetingActivity.class.getName()));
        release();
    }

    @Test
    public void _4addMeeting_shouldAddItem() {
        onView(withId(R.id.addMeetingFloatingActionButton)).perform(click());
        onView(withId(R.id.newMeetingTopicTextView)).perform(replaceText("TOPIC"));
        onView(withId(R.id.newMeetingParticipantsTextView)).perform(replaceText("NAME"));
        onView(withId(R.id.newMeetingSaveActionMaterialButton)).perform(click());

        onView(withId(R.id.meetingListRecyclerView))
                .check(matches(atPosition(itemCount, hasDescendant(withText(containsString("TOPIC"))))));
        itemCount += 1;
    }

    @Test
    public void _3StopFilteringButton_Should_Stop_Filter() {
        onView(allOf(withId(R.id.filterImageButton), isDisplayed())).perform(click());
        onView(withId(R.id.fragmentFilterMeetingByTimeButton)).perform(click());
        onView(allOf(withId(R.id.meetingListRecyclerView),isDisplayed())).check(withItemCount(itemCount));
    }



    @Test
    public void _1FilterMeetingsByDate_Should_Filter() {
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        
        onView(allOf(withId(R.id.filterImageButton), isDisplayed())).perform(click());
        //filtering out meetings that don't happen today (the default when the DatePicker shows up)
        onView(withId(R.id.fragmentFilterMeetingsLinearLayout)).perform(ViewActions.pressBack());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM");
        String textToCheck = dateFormat.format(time);

        onView(withId(R.id.meetingListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(textToCheck))))));
    }

    @Test
    public void _2FilterMeetingsByLocation_Should_Filter() {
        onView(allOf(withId(R.id.filterImageButton), isDisplayed())).perform(click());
        onView(withId(R.id.fragmentFilterMeetingsLinearLayout)).perform(swipeLeft());

        onView(withId(R.id.fragmentFilterMeetingByLocationRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.fragmentFilterMeetingsTitleTextView)).perform(ViewActions.pressBack());

        Context context = mActivityRule.getActivity().getBaseContext();
        String textToCheck = context.getResources().getStringArray(R.array.meetingRoomList)[1];

        onView(withId(R.id.meetingListRecyclerView))
                .check(matches(atPosition(0, hasDescendant(withText(containsString(textToCheck))))));
    }

}