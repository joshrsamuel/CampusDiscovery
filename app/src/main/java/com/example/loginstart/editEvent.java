package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class editEvent extends AppCompatActivity {
    EditText title;
    EditText eventDescription;
    EditText location;
    EditText time;
    Button update;
    Button returnToDashBtn;
    String txtTitle, txtEventDescription, txtLocation, txtTime;
    DatabaseReference mirajDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        update = (Button) findViewById(R.id.createBtn);
        update.setText("Update");

        String titleString = getIntent().getStringExtra("title");
        String descriptionString = getIntent().getStringExtra("description");
        String locationString = getIntent().getStringExtra("location");
        String timeString = getIntent().getStringExtra("time");
        String host = getIntent().getStringExtra("host");

        title = (EditText) findViewById(R.id.title);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        location = (EditText) findViewById(R.id.location);
        time = (EditText) findViewById(R.id.time);

        title.setText(titleString);
        eventDescription.setText(descriptionString);
        location.setText(locationString);
        time.setText(timeString);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitle = title.getText().toString().trim();
                txtEventDescription = eventDescription.getText().toString().trim();
                txtLocation = location.getText().toString().trim();
                txtTime = time.getText().toString().trim();

                mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");

                mirajDatabase.child(titleString).removeValue();
                Map<String, Object> updates = new HashMap<>();
                updates.put(txtTitle, new Event(txtTitle, host, txtEventDescription, txtLocation, txtTime));
                mirajDatabase.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(editEvent.this, "Successfully updated event.", Toast.LENGTH_LONG).show();
                            String caller = getIntent().getStringExtra("class");
                            doReturn(caller);
                        } else {
                            Toast.makeText(editEvent.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        returnToDashBtn = (Button) findViewById(R.id.returnToDash);
        returnToDashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caller = getIntent().getStringExtra("class");
                doReturn(caller);
            }
        });
    }

    private void doReturn(String caller) {
        switch (caller) {
            case "student":
                startActivity(new Intent(editEvent.this, student.class));
                break;
            case "teacher":
                startActivity(new Intent(editEvent.this, teacher.class));
                break;
        }
    }
}
