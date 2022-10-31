package com.example.loginstart;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class Event {
    String title;
    FirebaseUser host;
    String eventDescription;
    String location;
    String time;

    public Event(String title, FirebaseUser host, String eventDescription, String location, String time) {
        this.title = title;
        this.host = host;
        this.eventDescription = eventDescription;
        this.location = location;
        this.time = time;
    }

    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
