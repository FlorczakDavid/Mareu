package com.florczakdavid.maru.DI;

import com.florczakdavid.maru.model.Meeting;

import java.util.ArrayList;
import java.util.List;

public interface MeetingApiService {

    List<Meeting> getMeetings();
    List<Meeting> getFilteredMeetings();

    boolean isMeetingListBeingFiltered();

    void stopFilteringMeetings();

    void deleteMeeting(Meeting meeting);

    void createMeeting(Meeting meeting);

    void filterMeetings(Meeting[] meetings);
}
