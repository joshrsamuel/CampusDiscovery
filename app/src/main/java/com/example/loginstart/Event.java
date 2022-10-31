package com.example.loginstart;

import com.google.firebase.auth.UserInfo;

public class Event {
    String title;
    UserInfo host;
    String eventDescription;
    String location;
    String time;

    public Event(String title, UserInfo host, String eventDescription, String location, String time) {
        this.title = title;
        this.host = host;
        this.eventDescription = eventDescription;
        this.location = location;
        this.time = time;
    }
}
