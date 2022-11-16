package com.example.loginstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class student extends AppCompatActivity implements RecyclerViewInterface {
    private Button exitBtn;
    private FloatingActionButton createEventBtn;
    private DatabaseReference mirajDatabase;
    private FirebaseUser currUser;
    private TextView dashboardHeader;
    private userInfo currUserInfo;
    private Button nextBtn;
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mirajDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        mirajDatabase.child(currUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currUserInfo = snapshot.getValue(userInfo.class);
                if (currUserInfo != null) {
                    if (currUserInfo.getUserType().equals("Admin")) {
                        dashboardHeader = (TextView)  findViewById(R.id.eventHeader);
                        dashboardHeader.setText("Admin Dashboard");
                        FloatingActionButton tempBtn = (FloatingActionButton) findViewById(R.id.studentCreate);
                        tempBtn.setVisibility(View.GONE);
                    } else if (currUserInfo.getUserType().equals("Teacher")) {
                        dashboardHeader = (TextView) findViewById(R.id.eventHeader);
                        dashboardHeader.setText("Teacher Dashboard");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        exitBtn = (Button) findViewById(R.id.quit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(student.this, MainActivity.class));
            }
        });

        createEventBtn = (FloatingActionButton) findViewById(R.id.studentCreate);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(student.this, createEvent.class).putExtra("class", "student"));
            }
        });
        mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");
        final int[] currPage = new int[1];
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Page> pages = new ArrayList<>();
                ArrayList<DataSnapshot> childData = new ArrayList<>();
                currPage[0] = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    childData.add(child);
                }
                int numPages;
                if (childData.size() % 10 == 0) {
                    numPages = childData.size() / 10;
                } else {
                    numPages = childData.size() / 10 + 1;
                }
                int currChild = 0;
                int pageNum = 1;
                if (numPages == 0) {
                    pages.add(new Page(childData, pageNum));
                }
                for (int i = 0; i < numPages; i++) {
                    if (i == numPages - 1) {
                        pages.add(new Page(childData.subList(currChild, childData.size()), pageNum));
                        break;
                    } else {
                        pages.add(new Page(childData.subList(currChild, currChild + 10), pageNum));
                        currChild += 10;
                        pageNum += 1;
                    }
                }

                RecyclerView recyclerView = findViewById(R.id.recycleviewstudent);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(0).getData(), context, student.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                Toast.makeText(student.this, "Events successfully loaded.", Toast.LENGTH_SHORT).show();

                nextBtn = (Button) findViewById(R.id.nextPageStud);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currPage[0] + 1 == pages.size()) {
                            Toast.makeText(student.this, "No next page.", Toast.LENGTH_SHORT).show();
                        } else {
                            currPage[0] += 1;
                            RecyclerView recyclerView = findViewById(R.id.recycleviewstudent);
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), context, student.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(student.this));
                        }
                    }
                });
                backBtn = (Button) findViewById(R.id.backPageStud);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currPage[0] == 0) {
                            Toast.makeText(student.this, "No previous page.", Toast.LENGTH_SHORT).show();
                        } else {
                            currPage[0] -= 1;
                            RecyclerView recyclerView = findViewById(R.id.recycleviewstudent);
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), context, student.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(student.this));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(student.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
            }
        };
        mirajDatabase.addValueEventListener(postListener);

    }

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickEdit(List<DataSnapshot> data, int position) {
        Intent edit = new Intent(this, editEvent.class);
        edit.putExtra("title", data.get(position).child("title").getValue(String.class));
        edit.putExtra("description", data.get(position).child("eventDescription").getValue(String.class));
        edit.putExtra("location", data.get(position).child("location").getValue(String.class));
        edit.putExtra("time", data.get(position).child("time").getValue(String.class));
        edit.putExtra("host", data.get(position).child("host").getValue(String.class));
        edit.putExtra("class", "student");
        edit.putExtra("capacity",data.get(position).child("capacity").getValue(Integer.class));
        edit.putExtra("inviteOnly", data.get(position).child("inviteOnly").getValue(Boolean.class));
        startActivity(edit);
    }

    @Override
    public void onItemClick(List<DataSnapshot> data, int position) {
        String theEmail = currUser.getEmail();
        String emailHead = theEmail.substring(0, theEmail.length()-4);
        System.out.println(emailHead);
        System.out.println(data.get(position).child("invitees").child(emailHead).getValue());
        if (data.get(position).child("invitees").child(emailHead) != null
                && data.get(position).child("invitees").child(emailHead).getValue() != null
                && data.get(position).child("invitees").child(emailHead).getValue(String.class).equals(theEmail)) {
            Intent rsvp = new Intent(this, rsvpEvent.class);

            rsvp.putExtra("title", data.get(position).child("title").getValue(String.class));
            rsvp.putExtra("description", data.get(position).child("eventDescription").getValue(String.class));
            rsvp.putExtra("location", data.get(position).child("location").getValue(String.class));
            rsvp.putExtra("time", data.get(position).child("time").getValue(String.class));
            rsvp.putExtra("host", data.get(position).child("host").getValue(String.class));
            rsvp.putExtra("attendees", data.get(position).child("attendees").child("Will Attend").getChildrenCount());
            rsvp.putExtra("class", "student");
            rsvp.putExtra("capacity", data.get(position).child("capacity").getValue(Integer.class));
            rsvp.putExtra("inviteOnly", data.get(position).child("inviteOnly").getValue(Boolean.class));
            startActivity(rsvp);
        } else {
            Toast.makeText(student.this, "Not invited to event.", Toast.LENGTH_LONG).show();
        }
    }
}