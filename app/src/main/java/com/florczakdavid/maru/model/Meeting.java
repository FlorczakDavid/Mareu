package com.florczakdavid.maru.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Meeting {
    private Date time;
    private String location;
    private String topic;
    private User[] participants;

    public Meeting(Date time, String location, String topic, User[] participants) {
        this.time = time;
        this.location = location;
        this.topic = topic;
        this.participants = participants;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public User[] getParticipants() {
        return participants;
    }

    public void setParticipants(User[] participants) {
        this.participants = participants;
    }
}
