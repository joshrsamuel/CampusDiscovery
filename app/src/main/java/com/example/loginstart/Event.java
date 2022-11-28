package com.example.loginstart;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class Event {
    public String title;
    public String host;
    public String eventDescription;
    public String location;
    public String startTime;
    public String endTime;
    public int capacity;
    public boolean inviteOnly;

    public Event(String title, String host, String eventDescription, String location, String startTime, String endTime, int capacity, boolean inviteOnly) {
        this.title = title;
        this.host = host;
        this.eventDescription = eventDescription;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.inviteOnly = inviteOnly;
    }

    public String toString() {
        System.out.println(getClass().getName() + "@" + Integer.toHexString(hashCode()));
        return "Event@" + Integer.toHexString(hashCode());
    }

    public String getTitle() {
        return title;
    }

    public String getHost() {
        return host;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setTitle(String newTitle) {
        title = newTitle;
    }

    public void setHost(String newHost) {
        host = newHost;
    }

    public void setEventDescription(String newEventDescription) {
        eventDescription = newEventDescription;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }

}
