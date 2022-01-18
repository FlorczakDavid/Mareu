package com.florczakdavid.maru.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FilterMeetingsPagerAdapter extends FragmentStateAdapter {

    public FilterMeetingsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        return position == 0 ? FilterMeetingByTimeFragment.newInstance() : FilterMeetingByLocationFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
