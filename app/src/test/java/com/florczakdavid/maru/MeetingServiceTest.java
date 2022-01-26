package com.florczakdavid.maru;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.florczakdavid.maru.DI.DI;
import com.florczakdavid.maru.DI.DummyMeetingGenerator;
import com.florczakdavid.maru.DI.MeetingApiService;
import com.florczakdavid.maru.model.Meeting;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
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

//            service.filterMeetings();
        }



    }