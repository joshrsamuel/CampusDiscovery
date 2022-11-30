package com.example.loginstart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
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
    Spinner location;
    EditText time;
    EditText capacity;
    Button update;
    TextView editHeader;
    TextView invitePpl;
    private Button yesBtn, laterBtn;
    private LinearLayout invBtns;
    Button returnToDashBtn;
    String txtTitle, txtEventDescription, txtLocation, txtTime;
    int capNum;
    Switch inviteOnly;
    boolean isInvite;
    DatabaseReference mirajDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        editHeader = (TextView) findViewById(R.id.orgHeader);
        editHeader.setText("Edit Event");
        update = (Button) findViewById(R.id.createBtn);
        update.setText("Update");
        inviteOnly = (Switch) findViewById(R.id.invite);
        inviteOnly.setVisibility(View.INVISIBLE);
        String[] locations;

        String titleString = getIntent().getStringExtra("title");
        String descriptionString = getIntent().getStringExtra("description");
        String locationString = getIntent().getStringExtra("location");
        String timeString = getIntent().getStringExtra("time");
        String host = getIntent().getStringExtra("host");
        int capX = getIntent().getIntExtra("capacity", 10);
        isInvite = getIntent().getBooleanExtra("inviteOnly", false);

        title = (EditText) findViewById(R.id.title);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        location = (Spinner) findViewById(R.id.location);
        locations = new String[]{"West Dorms", "CRC", "CRC Field", "Student Center",
                "Tech Green", "CULC", "Klaus", "CoC", "East Dorms", "NAve", "Bobby Dodd Stadium", "McCamish Pavilion", "Tech Square"};
        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locations);
        location.setAdapter(locationAdapter);
        time = (EditText) findViewById(R.id.time);
        capacity = (EditText) findViewById(R.id.capacity);
        invBtns = (LinearLayout) findViewById(R.id.invButtons);
        yesBtn = (Button) findViewById(R.id.addInvBtn);
        laterBtn = (Button) findViewById(R.id.laterBtn);
        invitePpl = (TextView) findViewById(R.id.invPplText);

        int selecIdx = 0;
        while (!locationString.equals(locations[selecIdx])) {
            selecIdx++;
        }

        title.setText(titleString);
        eventDescription.setText(descriptionString);
        location.setSelection(selecIdx);
        time.setText(timeString);
        capacity.setText(String.valueOf(capX));
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtTitle = title.getText().toString().trim();
                txtEventDescription = eventDescription.getText().toString().trim();
                txtLocation = location.getSelectedItem().toString().trim();
                txtTime = time.getText().toString().trim();
                capNum = Integer.valueOf(capacity.getText().toString().trim());

                mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");

                mirajDatabase.child(titleString).removeValue();
                Map<String, Object> updates = new HashMap<>();
                updates.put(txtTitle, new Event(txtTitle, host, txtEventDescription, txtLocation, txtTime, capNum, isInvite));
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
        if (isInvite) {
            isInvite = false;
            invitePpl.setVisibility(View.VISIBLE);
            invBtns.setVisibility(View.VISIBLE);
            yesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goInvite = new Intent(editEvent.this, addInvitees.class);
                    goInvite.putExtra("title", txtTitle);
                    goInvite.putExtra("userType", caller);
                    startActivity(goInvite);
                }
            });
            laterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String caller = getIntent().getStringExtra("class");
                    doReturn(caller);
                }
            });
        } else {
            switch (caller) {
                case "student":
                    startActivity(new Intent(editEvent.this, student.class));
                    break;
                case "teacher":
                    startActivity(new Intent(editEvent.this, student.class));
                    break;
            }
        }
    }
}
