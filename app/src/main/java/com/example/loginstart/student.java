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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class student extends AppCompatActivity {
    private Button exitBtn;
    private FloatingActionButton createEventBtn;
    private DatabaseReference mirajDatabase;
    private Button nextBtn;
    private Button backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
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
        ArrayList<DataSnapshot> childData= new ArrayList<>();
        ArrayList<Page> pages = new ArrayList<>();
        final int[] currPage = new int[1];
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(0).getData(), context);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                Toast.makeText(student.this, "Events successfully loaded.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(student.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
            }
        };
        mirajDatabase.addValueEventListener(postListener);
        nextBtn = (Button) findViewById(R.id.nextPageStud);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currPage[0] + 1 == pages.size()) {
                    Toast.makeText(student.this, "No next page.", Toast.LENGTH_LONG).show();
                } else {
                    currPage[0] += 1;
                    RecyclerView recyclerView = findViewById(R.id.recycleviewstudent);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), student.this);
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
                    Toast.makeText(student.this, "No previous page.", Toast.LENGTH_LONG).show();
                } else {
                    currPage[0] -= 1;
                    RecyclerView recyclerView = findViewById(R.id.recycleviewstudent);
                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), student.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(student.this));

                }
            }
        });
    }
    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}