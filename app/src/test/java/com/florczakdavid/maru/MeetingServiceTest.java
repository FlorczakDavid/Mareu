package com.florczakdavid.maru;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.DummyMeetingGenerator;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.model.Meeting;

import java.util.List;

@RunWith(JUnit4.class)
public class MeetingServiceTest {

        private MeetingApiService service;


        @Before
        public void setup() {
            service = DI.getNewInstanceApiService();
        }


        @Test
        public void getMeetingsWithSuccess() {
            List<Meeting> meetings = service.getMeetings();
            List<Meeting> expectedMeetings = DummyMeetingGenerator.DUMMY_MEETINGS;
            assertThat(meetings, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedMeetings.toArray()));
        }

        @Test
        public void deleteMeetingWithSuccess() {
            Meeting meetingToDelete = service.getMeetings().get(0);
            service.deleteMeeting(meetingToDelete);
            assertFalse(service.getMeetings().contains(meetingToDelete));
        }

        @Test
        public void createMeetingWithSuccess() {
            Meeting meetingToAdd = service.getMeetings().get(0);
            service.createMeeting(meetingToAdd);
            assertTrue(service.getMeetings().contains(meetingToAdd));
        }

        @Test
        public void filterMeetingsWithSuccess() {
            Meeting[] meetingsToFilter = {service.getMeetings().get(0)};
            service.filterMeetings(meetingsToFilter);
            assertTrue(service.getFilteredMeetings().stream().count() < service.getMeetings().stream().count());
            assertThat(service.getFilteredMeetings(), IsIterableContainingInAnyOrder.containsInAnyOrder(meetingsToFilter));
        }
    }