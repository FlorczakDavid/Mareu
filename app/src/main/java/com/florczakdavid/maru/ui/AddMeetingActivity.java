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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMeetingActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getMeetingApiService();
        calendar = Calendar.getInstance();
        roomList = getResources().getStringArray(R.array.meetingRoomList);
        setContentView(R.layout.activity_add_meeting);
        ButterKnife.bind(this);

        mNewMeetingPickDateDatePicker.findViewById(Resources.getSystem().getIdentifier("year", "id", "android")).setVisibility(View.GONE);

        refreshTimePickingLayout();
        mNewMeetingPickDateTimePicker.setIs24HourView(true);
        mNewMeetingPickDateTimePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(
                        mNewMeetingPickDateDatePicker.getYear(),
                        mNewMeetingPickDateDatePicker.getMonth(),
                        mNewMeetingPickDateDatePicker.getDayOfMonth(),
                        hourOfDay,minute
                );
                setTime(calendar.getTime());
            }
        });

        mNewMeetingPickDateDatePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth,
                        mNewMeetingPickDateTimePicker.getCurrentHour(),
                        mNewMeetingPickDateTimePicker.getCurrentMinute()
                );
                setTime(calendar.getTime());
            }
        });

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNewMeetingLocationsListSpinner.setAdapter(adapter);
    }

    void setTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, d MMM HH:mm");
        SimpleDateFormat endFormat = new SimpleDateFormat("HH:mm");
        long endDate = date.getTime();
        endDate += 45 * 60 * 1000;
        Date end = new Date(endDate);
        mNewMeetingConditionsTextView.setText(simpleDateFormat.format(date) + " - " + endFormat.format(end));

        roomList = getResources().getStringArray(R.array.meetingRoomList);
        String[] unavailableRooms = getUnavailableRoomsList(date, getBusyTimeRange());
        ArrayList<String> newRoomList = new ArrayList<String>(Arrays.asList(roomList));
        newRoomList.removeAll(Arrays.asList(unavailableRooms));
        roomList = (String[]) newRoomList.toArray(new String[newRoomList.size()]); // ???
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, roomList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNewMeetingLocationsListSpinner.setAdapter(adapter);
    }

    ArrayList<Date[]> getBusyTimeRange() {
        ArrayList<Date[]> timeRange = new ArrayList<>();
        for (Meeting meeting: mApiService.getMeetings()) {
            Date beginDate = meeting.getTime();
            long endDateLong = beginDate.getTime() + 45 * 60 * 1000;
            Date endDate = new Date(endDateLong);
            timeRange.add(new Date[]{beginDate, endDate});
        }
        return timeRange;
    }

    String[] getUnavailableRoomsList(Date chosenDate, ArrayList<Date[]> listOfBusyDates) {
        ArrayList<String> unavailableRoomList = new ArrayList<String>();
        long chosenDateBegin = chosenDate.getTime();
        long chosenDateEnd = chosenDateBegin + (45 * 60 * 1000);

        for (int i=0; i<listOfBusyDates.size(); i++) {
            Date[] busyDate = listOfBusyDates.get(i);
            if ((busyDate[1].getTime() > chosenDateBegin && busyDate[0].getTime() < chosenDateBegin) ||
                    (busyDate[0].getTime() < chosenDateEnd && busyDate[1].getTime() > chosenDateEnd)) {
                unavailableRoomList.add(mApiService.getMeetings().get(i).getLocation());
            }
        }

        return (String[]) unavailableRoomList.toArray(new String[unavailableRoomList.size()]); // ???
    }

    public static void navigate(FragmentActivity activity) {
        Intent intent = new Intent(activity, AddMeetingActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
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
        mApiService.createMeeting(meeting);



        finish();
    }

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