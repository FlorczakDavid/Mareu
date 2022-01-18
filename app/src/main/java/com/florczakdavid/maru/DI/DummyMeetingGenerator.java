package com.florczakdavid.maru.DI;

import com.florczakdavid.maru.R;
import com.florczakdavid.maru.model.Meeting;
import com.florczakdavid.maru.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public abstract class DummyMeetingGenerator {

    static public List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(new Date(), "Meeting Room A", "topic 1", new User[] {new User("abc"), new User("def")}),
            new Meeting(new Date(new Date().getTime()+86400000), "Meeting Room B", "topic 2", new User[] {new User("ghi")}),
            new Meeting(new Date(new Date().getTime()+864000000), "Meeting Room C", "topic 3", new User[] {new User("jkl"), new User("mno"), new User("pqr"), new User("stu")})
    );

    static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
