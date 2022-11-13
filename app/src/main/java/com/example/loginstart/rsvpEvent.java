package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class rsvpEvent extends AppCompatActivity{
    Button returnToDashBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp_event);
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
