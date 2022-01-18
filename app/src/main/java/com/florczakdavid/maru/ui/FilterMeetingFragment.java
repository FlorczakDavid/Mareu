package com.florczakdavid.maru.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.florczakdavid.maru.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FilterMeetingFragment extends BottomSheetDialogFragment {

    FilterMeetingsPagerAdapter mPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filter_meeting, container, false);

        TabLayout tabLayout = v.findViewById(R.id.fragmentFilterMeetingsTabLayout);
        ViewPager2 viewPager = v.findViewById(R.id.fragmentFilterMeetingsViewPager);

        mPagerAdapter = new FilterMeetingsPagerAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(mPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(position == 0 ? R.string.filterMeetingFragmentTimeTabTitle : R.string.filterMeetingFragmentLocationTabTitle)
        ).attach();

        return v;
    }
}
