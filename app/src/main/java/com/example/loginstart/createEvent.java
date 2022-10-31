package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class createEvent extends AppCompatActivity {
    private EditText title;
    private EditText host;
    private EditText eventDescription;
    private EditText location;
    private EditText time;
    private String txtTitle, txtHost, txtEventDescription, txtLocation, txtTime;
    private Button createBtn;
    private Button returnToDashBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        title = (EditText) findViewById(R.id.title);
        host = (EditText) findViewById(R.id.host);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        location = (EditText) findViewById(R.id.location);
        time = (EditText) findViewById(R.id.time);

        returnToDashBtn = (Button) findViewById(R.id.returnToDash);
        returnToDashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caller = getIntent().getStringExtra("class");

                switch (caller) {
                    case "student":
                        startActivity(new Intent(createEvent.this, student.class));
                        break;
                    case "teacher":
                        startActivity(new Intent(createEvent.this, teacher.class));
                        break;
                }
            }
        });

        createBtn = (Button) findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitle = title.getText().toString().trim();
                txtHost = host.getText().toString().trim();
                txtEventDescription = eventDescription.getText().toString().trim();
                txtLocation = location.getText().toString().trim();
                txtTime = time.getText().toString().trim();
            }
        });

    }

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
