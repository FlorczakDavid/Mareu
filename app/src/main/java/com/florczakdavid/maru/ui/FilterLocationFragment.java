package com.florczakdavid.maru.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.florczakdavid.maru.R;

import java.util.zip.Inflater;

public class FilterLocationFragment extends Fragment {

    FilterByLocationListRecycleViewAdapter mFilterByLocationListRecycleViewAdapter;

    public static FilterLocationFragment newInstance() {
        FilterLocationFragment fragment = new FilterLocationFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_meeting_by_location, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.fragmentFilterMeetingByLocationRecyclerView);
        mFilterByLocationListRecycleViewAdapter = new FilterByLocationListRecycleViewAdapter(this.getContext());
        recyclerView.setAdapter(mFilterByLocationListRecycleViewAdapter);

        return  v;
    }
}
