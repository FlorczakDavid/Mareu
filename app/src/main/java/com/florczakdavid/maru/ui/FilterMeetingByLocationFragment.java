package com.florczakdavid.maru.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.R;
import com.florczakdavid.maru.model.Meeting;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FilterMeetingByLocationFragment extends Fragment {

    private FilterByLocationListRecycleViewAdapter mFilterByLocationListRecycleViewAdapter;
    private MeetingApiService mApiService;

    private RecyclerView recyclerView;
    private Button mButton;

    private boolean isVisible;
    private boolean isFiltering;

    public static FilterMeetingByLocationFragment newInstance() {
        FilterMeetingByLocationFragment fragment = new FilterMeetingByLocationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mApiService = DI.getMeetingApiService();
        isFiltering = true;
        View v = inflater.inflate(R.layout.fragment_filter_meeting_by_location, container, false);

        recyclerView = v.findViewById(R.id.fragmentFilterMeetingByLocationRecyclerView);
        mButton = v.findViewById(R.id.fragmentFilterMeetingByLocationButton);

        mFilterByLocationListRecycleViewAdapter = new FilterByLocationListRecycleViewAdapter(this.getContext());

        recyclerView.setAdapter(mFilterByLocationListRecycleViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mApiService.stopFilteringMeetings();
                isFiltering = false;
                ((BottomSheetDialogFragment) getParentFragment()).dismiss();
                ((MainActivity)getActivity()).init();
            }
        });

        return  v;
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

        FilterByLocationListRecycleViewAdapter adapter = (FilterByLocationListRecycleViewAdapter) recyclerView.getAdapter();
        List<String> selectedRooms = adapter.getSelectedRooms();

        for(String room : selectedRooms) {
            for(Meeting meeting : meetingList) {
                if(meeting.getLocation().equals(room)) {
                    meetingsToKeep.add(meeting);
                }
            }
        }

        mApiService.filterMeetings((Meeting[]) meetingsToKeep.toArray(new Meeting[meetingsToKeep.size()]));
        ((MainActivity)getActivity()).init();
    }
}
