package com.florczakdavid.maru.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.R;
import com.florczakdavid.maru.model.Meeting;
import com.florczakdavid.maru.model.User;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeetingActivity extends AppCompatActivity {

    // UI Components
    @BindView(R.id.newMeetingConditionsTextView)
    TextView mNewMeetingConditionsTextView;
    @BindView(R.id.newMeetingPickDateImageButton)
    ImageButton mNewMeetingPickDateImageButton;
    @BindView(R.id.newMeetingTopicTextView)
    EditText mNewMeetingTopicTextView;
    @BindView(R.id.newMeetingLocationsListSpinner)
    Spinner mNewMeetingLocationsListSpinner;
    @BindView(R.id.newMeetingSaveActionMaterialButton)
    MaterialButton mNewMeetingSaveActionMaterialButton;
    @BindView(R.id.newMeetingParticipantsTextView)
    EditText mNewMeetingParticipantsTextView;

    @BindView(R.id.newMeetingPickDateLinearLayout)
    LinearLayout mNewMeetingPickDateLinearLayout;
    @BindView(R.id.newMeetingPickDateDatePicker)
    DatePicker mNewMeetingPickDateDatePicker;
    @BindView(R.id.newMeetingPickDateTimePicker)
    TimePicker mNewMeetingPickDateTimePicker;

    private boolean isTimePickingShown = false;
    private MeetingApiService mApiService;
    private String[] roomList;
    private Calendar calendar;
    private List<Meeting> mMeetingList;

    final static long MEETING_DURATION = 45 * 60 * 1000; //meeting length fixed at 45 minutes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);

        mApiService = DI.getMeetingApiService();
        mMeetingList = mApiService.getMeetings();
        roomList = getResources().getStringArray(R.array.meetingRoomList);

        initDatePicker();
        initRoomList();
    }

    void initRoomList() {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>( this, android.R.layout.simple_spinner_item, roomList );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        mNewMeetingLocationsListSpinner.setAdapter( adapter );
    }

    void initDatePicker() {
        calendar = Calendar.getInstance();
        setTime(calendar.getTime());
        mNewMeetingPickDateDatePicker.findViewById( Resources.getSystem().getIdentifier( "year", "id", "android" ) ).setVisibility( View.GONE );

        refreshTimePickingLayout();
        mNewMeetingPickDateTimePicker.setIs24HourView( true );
        mNewMeetingPickDateTimePicker.setOnTimeChangedListener( new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(
                        mNewMeetingPickDateDatePicker.getYear(),
                        mNewMeetingPickDateDatePicker.getMonth(),
                        mNewMeetingPickDateDatePicker.getDayOfMonth(),
                        hourOfDay, minute
                );
                setTime( calendar.getTime() );
            }
        } );

        mNewMeetingPickDateDatePicker.init( calendar.get( Calendar.YEAR ), calendar.get( Calendar.MONTH ), calendar.get( Calendar.DAY_OF_MONTH ), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set( year, monthOfYear, dayOfMonth,
                        mNewMeetingPickDateTimePicker.getCurrentHour(),
                        mNewMeetingPickDateTimePicker.getCurrentMinute()
                );
                setTime( calendar.getTime() );
            }
        } );
    }

    //manages mNewMeetingConditionsTextView text display and mNewMeetingLocationsListSpinner list content
    void setTime(Date date) {
        //Set meeting range display format to look like this: Mon, 01 Jan 00:00 - 00:45
        SimpleDateFormat beginDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
        SimpleDateFormat endDateFormat = new SimpleDateFormat("HH:mm");
        //add 45 minutes to selected date to be the meeting's end date and set the textview to display these dates
        Date endDate = new Date(date.getTime() + MEETING_DURATION);
        mNewMeetingConditionsTextView.setText(beginDateFormat.format(date) + " - " + endDateFormat.format(endDate));

        //filter out the unavailable rooms at selected date so they don't show in them in the room list
        //first get list of all rooms
        roomList = getResources().getStringArray(R.array.meetingRoomList);
        //make a list of unavailable rooms using getBusyTimeRange() and getUnavailableRoomsList(Date, ArrayList<Date[]>) bellow
        String[] unavailableRooms = getUnavailableRoomsList(date, getBusyTimeRange());
        //convert roomList from String[] to ArrayList<String> so I can filter out the unavailableRooms using removeAll()
        ArrayList<String> newRoomList = new ArrayList<String>(Arrays.asList(roomList));
        newRoomList.removeAll(Arrays.asList(unavailableRooms));
        //convert newRoomList back to String[] so it will fit the ArrayAdapter's constructor and create it
        roomList = (String[]) newRoomList.toArray(new String[newRoomList.size()]); // ???
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, roomList);
        //configure the adapter and link it to mNewMeetingLocationsListSpinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNewMeetingLocationsListSpinner.setAdapter(adapter);
    }

    //simple function returning the time ranges of all saved meetings
    ArrayList<Date[]> getBusyTimeRange() {
        ArrayList<Date[]> timeRange = new ArrayList<>();
        for (Meeting meeting: mMeetingList) {
            Date beginDate = meeting.getTime();
            long endDateLong = beginDate.getTime() + MEETING_DURATION;
            Date endDate = new Date(endDateLong);
            timeRange.add(new Date[]{beginDate, endDate});
        }
        return timeRange;
    }

    //returns the list of rooms occupied at given time
    String[] getUnavailableRoomsList(Date chosenDate, ArrayList<Date[]> listOfBusyDates) {
        ArrayList<String> unavailableRoomList = new ArrayList<String>();
        long chosenDateBegin = chosenDate.getTime();
        long chosenDateEnd = chosenDateBegin + MEETING_DURATION; //45 minutes

        for (int i=0; i<listOfBusyDates.size(); i++) {
            Date[] busyDate = listOfBusyDates.get(i);
            //a room is unavailable if it has a meeting at these time ranges:
            //      |----|
            //  |----|  |----|
            //(the case of a time range containing the other isn't checked because the meeting length is set to 45 minutes)

            //either the selected date's beginning (long) is earlier (smaller) than the checked date beginning
            //while the selected date's ending (long) is later (bigger) than the checked date beginning
            //or
            //the selected date's beginning (long) is earlier (smaller) than the checked date ending
            //while the selected date's ending (long) is later (bigger) than the checked date ending

            //busyDate[0] is the checked date beginning
            //busyDate[1] is the checked date ending
            if ((busyDate[1].getTime() > chosenDateBegin && busyDate[0].getTime() < chosenDateBegin) ||
                    (busyDate[0].getTime() < chosenDateEnd && busyDate[1].getTime() > chosenDateEnd)) {
                unavailableRoomList.add(mMeetingList.get(i).getLocation());
            }
        }

        return (String[]) unavailableRoomList.toArray(new String[unavailableRoomList.size()]); // ???
    }

    @OnClick(R.id.newMeetingPickDateImageButton)
    void showTimePicking() {
        isTimePickingShown = !isTimePickingShown;
        refreshTimePickingLayout();
    }

    void refreshTimePickingLayout() {
        TransitionManager.beginDelayedTransition(
                (ViewGroup) findViewById(android.R.id.content).getRootView(),
                new TransitionSet().addTransition(new ChangeBounds())
        );

        ViewGroup.LayoutParams linearLayout = mNewMeetingPickDateLinearLayout.getLayoutParams();
        linearLayout.height = isTimePickingShown ? ConstraintLayout.LayoutParams.WRAP_CONTENT : 1;
        mNewMeetingPickDateLinearLayout.setLayoutParams(linearLayout);
    }

    //transcribe displayed info into Meeting object
    @OnClick(R.id.newMeetingSaveActionMaterialButton)
    void addMeetingToList() {
        calendar.set(
                mNewMeetingPickDateDatePicker.getYear(),
                mNewMeetingPickDateDatePicker.getMonth(),
                mNewMeetingPickDateDatePicker.getDayOfMonth(),
                mNewMeetingPickDateTimePicker.getCurrentHour(),
                mNewMeetingPickDateTimePicker.getCurrentMinute()
        );

        Date meetingDate = calendar.getTime();
        String meetingLocation = (String) mNewMeetingLocationsListSpinner.getSelectedItem();
        String meetingTopic = mNewMeetingTopicTextView.getText().toString();
        User[] meetingUsers = quickUserCreation(mNewMeetingParticipantsTextView.getText().toString());

        Meeting meeting = new Meeting(meetingDate, meetingLocation, meetingTopic, meetingUsers);

        if(meeting.getLocation().equals("null")) {
            Toast.makeText(this, "no available rooms at selected time", Toast.LENGTH_SHORT).show();
            return;
        }

        if(meeting.getTopic().equals("")) {
            Toast.makeText(this, "please write a topic", Toast.LENGTH_SHORT).show();
            return;
        }

        if(meeting.getParticipants()[0].getName().equals("")) {
            Toast.makeText(this, "at least one participant is required", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.createMeeting(meeting);

        finish();
    }

    //makes a list of users from single string, all usernames should be separated by commas
    User[] quickUserCreation(String userNamesListInOneString) {
        ArrayList<User> userListToReturn = new ArrayList<User>();
        String[] userNamesListInArray = userNamesListInOneString.split(",");

        for (int i = 0; i < userNamesListInArray.length; i++) {
            User user = new User(userNamesListInArray[i]);;
            userListToReturn.add(user);
        }

        return (User[]) userListToReturn.toArray(new User[userListToReturn.size()]); // ????
    }
}