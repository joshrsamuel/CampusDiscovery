package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class rsvpEvent extends AppCompatActivity{
    TextView title;
    TextView location;
    TextView time;
    TextView description;
    TextView host;
    DatabaseReference userDatabase;
    Button returnToDashBtn;
    Button RSVPBtn;
    FirebaseUser currUser;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp_event);

        title = (TextView) findViewById(R.id.titleRSVP);
        location = (TextView) findViewById(R.id.locationRSVP);
        time = (TextView) findViewById(R.id.timeRSVP);
        description = (TextView) findViewById(R.id.descriptionRSVP);
        host = (TextView) findViewById(R.id.hostRSVP);

        String titleString = getIntent().getStringExtra("title");
        String descriptionString = getIntent().getStringExtra("description");
        String locationString = getIntent().getStringExtra("location");
        String timeString = getIntent().getStringExtra("time");
        String hostString = getIntent().getStringExtra("host");

        DatabaseReference userDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
        userDatabase.child(hostString).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo eventHost = snapshot.getValue(userInfo.class);
                if (eventHost !=null) {
                    host.setText(eventHost.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed");
            }
        });

        title.setText(titleString);
        location.setText(locationString);
        time.setText(timeString);
        description.setText(descriptionString);
        final String[] username = new String[1];

        currUser = FirebaseAuth.getInstance().getCurrentUser();
        userDatabase.child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInfo user = snapshot.getValue(userInfo.class);
                username[0] = user.getFullName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed to get username");
            }
        });

        DatabaseReference eventDatabase = FirebaseDatabase.getInstance().getReference("Events");
        
        RSVPBtn = (Button) findViewById(R.id.RSVP);
        RSVPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventDatabase.child(titleString).child("attendees").child(currUser.getUid()).setValue(username[0]);
            }
        });

        returnToDashBtn = (Button) findViewById(R.id.RSVPreturn);
        returnToDashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caller = getIntent().getStringExtra("class");
                doReturn(caller);
            }
        });
    }
    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void doReturn(String caller) {
        switch (caller) {
            case "student":
                startActivity(new Intent(rsvpEvent.this, student.class));
                break;
            case "teacher":
                startActivity(new Intent(rsvpEvent.this, teacher.class));
                break;
        }
    }
}
