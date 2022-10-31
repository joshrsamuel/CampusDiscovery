package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class createEvent extends AppCompatActivity {
    private EditText title;
    private EditText eventDescription;
    private EditText location;
    private EditText time;
    private String txtTitle, txtEventDescription, txtLocation, txtTime;
    private Button createBtn;
    private Button returnToDashBtn;
    private Event created;
    private FirebaseUser currUser;
    private DatabaseReference mirajDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        title = (EditText) findViewById(R.id.title);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        location = (EditText) findViewById(R.id.location);
        time = (EditText) findViewById(R.id.time);

        returnToDashBtn = (Button) findViewById(R.id.returnToDash);
        returnToDashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String caller = getIntent().getStringExtra("class");
                System.out.println("Return registered");
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
                System.out.println("Create registered");
                txtTitle = title.getText().toString().trim();
                txtEventDescription = eventDescription.getText().toString().trim();
                txtLocation = location.getText().toString().trim();
                txtTime = time.getText().toString().trim();
                currUser = FirebaseAuth.getInstance().getCurrentUser();
                created = new Event(txtTitle, currUser, txtEventDescription, txtLocation, txtTime);
                mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");
                mirajDatabase.child(created.toString()).setValue(created).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(createEvent.this, "Successfully created.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(createEvent.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
