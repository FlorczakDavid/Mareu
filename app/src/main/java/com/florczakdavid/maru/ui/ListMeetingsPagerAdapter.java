package com.florczakdavid.maru.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ListMeetingsPagerAdapter extends FragmentPagerAdapter {


    public ListMeetingsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return position == 0 ? FilterTimeFragment.newInstance() : FilterLocationFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
