package com.example.loginstart;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class Event {
    public String title;
    public String host;
    public String eventDescription;
    public String location;
    public String time;
    public int capacity;
    public boolean inviteOnly;

    public Event(String title, String host, String eventDescription, String location, String time, int capacity, boolean inviteOnly) {
        this.title = title;
        this.host = host;
        this.eventDescription = eventDescription;
        this.location = location;
        this.time = time;
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

    public String getTime() {
        return time;
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

    public void setTime(String newTime) {
        time = newTime;
    }
}
