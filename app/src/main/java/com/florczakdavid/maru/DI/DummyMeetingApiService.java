package com.florczakdavid.maru.DI;

import com.florczakdavid.maru.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyMeetingApiService implements  MeetingApiService {

    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();
    private ArrayList<Meeting> filteredMeetings = new ArrayList<>();
    private boolean isMeetingListBeingFiltered = false;

    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public List<Meeting> getFilteredMeetings() {
        return filteredMeetings;
    }

    @Override
    public boolean isMeetingListBeingFiltered() {
        return isMeetingListBeingFiltered;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
        if(isMeetingListBeingFiltered) { filteredMeetings.remove(meeting); }
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public void filterMeetings(Meeting[] meetings) {
        filteredMeetings = new ArrayList<Meeting>(Arrays.asList(meetings));
        isMeetingListBeingFiltered = true;
    }

    @Override
    public void stopFilteringMeetings() {
        isMeetingListBeingFiltered = false;
    }
}
