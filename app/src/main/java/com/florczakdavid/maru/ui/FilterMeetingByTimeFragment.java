package com.florczakdavid.maru.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.R;
import com.florczakdavid.maru.model.Meeting;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FilterMeetingByTimeFragment extends Fragment {

    // UI Components (I am not using ButterKnife in this case as I already understand and want to use a different method for this part of this school project)
    private Button stopFilteringButton;
    private DatePicker mDatePicker;

    private Calendar calendar = Calendar.getInstance();
    private MeetingApiService mApiService;
    private boolean isVisible;
    private boolean isFiltering;

    public static FilterMeetingByTimeFragment newInstance() {
        FilterMeetingByTimeFragment fragment = new FilterMeetingByTimeFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApiService = DI.getMeetingApiService();
        isFiltering = true;
        View v = inflater.inflate(R.layout.fragment_filter_meeting_by_time, container, false);

        mDatePicker = v.findViewById(R.id.fragmentFilterMeetingByTimeDatePicker);
        stopFilteringButton = v.findViewById(R.id.fragmentFilterMeetingByTimeButton);

        stopFilteringButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiService.stopFilteringMeetings();
                isFiltering = false;
                ((BottomSheetDialogFragment) getParentFragment()).dismiss();
                ((MainActivity)getActivity()).init();
            }
        });

        return v;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        this.isVisible = menuVisible;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(!isVisible || !isFiltering) { return; }

        List<Meeting> meetingList = mApiService.getMeetings();
        List<Meeting> meetingsToKeep = new ArrayList<>();

        List<Date> selectedDate = pickedTimeRange();

            for(Meeting meeting : meetingList) {
                calendar.setTime(meeting.getTime());
                Date meetingStartTime = calendar.getTime();
                calendar.add(Calendar.MINUTE, 45);
                Date meetingEndTime = calendar.getTime();
                if (selectedDate.get(0).before(meetingStartTime) && selectedDate.get(1).after(meetingEndTime)) {
                    meetingsToKeep.add(meeting);
            }
        }

        mApiService.filterMeetings((Meeting[]) meetingsToKeep.toArray(new Meeting[meetingsToKeep.size()]));
        ((MainActivity)getActivity()).init();
    }

    List<Date> pickedTimeRange() {
        int day = mDatePicker.getDayOfMonth();
        int month = mDatePicker.getMonth();
        int year =  mDatePicker.getYear();
        calendar.set(year, month, day, 0, 0, 0);
        Date pickedDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date endDate = calendar.getTime();

        return List.of(new Date[]{pickedDate, endDate});
    }
}
