package com.florczakdavid.maru.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.R;
import com.florczakdavid.maru.model.Meeting;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import com.florczakdavid.maru.DI.DeleteMeetingEvent;
import com.google.android.material.snackbar.BaseTransientBottomBar;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

//    public ArrayList<Room>;

    // UI Components
    @BindView(R.id.meetingListRecyclerView)
    RecyclerView mMeetingListRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.filterImageButton)
    ImageButton mFilterImageButton;
    @BindView(R.id.addMeetingFloatingActionButton)
    FloatingActionButton mAddMeetingFloatingActionButton;

    private List<Meeting> meetings = new ArrayList<Meeting>();
    private MeetingListRecycleViewAdapter adapter;
    private MeetingApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiService = DI.getMeetingApiService();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    void init() {
        meetings = mApiService.isMeetingListBeingFiltered() ? mApiService.getFilteredMeetings() : mApiService.getMeetings();

        mMeetingListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MeetingListRecycleViewAdapter(this, meetings);
        mMeetingListRecyclerView.setAdapter(adapter);

        mFilterImageButton.setColorFilter(mApiService.isMeetingListBeingFiltered() ? Color.MAGENTA : Color.WHITE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        mApiService.deleteMeeting(event.meeting);
        init();
    }

    @OnClick(R.id.filterImageButton)
    void filterMeetings() {
        FilterMeetingFragment filterMeetingFragment = new FilterMeetingFragment();
        filterMeetingFragment.show(getSupportFragmentManager(), "ModalBottomSheet");
    }


    @OnClick(R.id.addMeetingFloatingActionButton)
    void addNeighbour() {
        AddMeetingActivity.navigate(this);
    }
}