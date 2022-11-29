package com.example.loginstart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyEvents extends AppCompatActivity {
    private Button exitBtn;
    private Button nextBtn;
    private Button backBtn;
    private DatabaseReference mirajDatabase;
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);
        exitBtn = (Button) findViewById(R.id.eventsQuit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MyEvents.this, student.class));
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

                RecyclerView recyclerView = findViewById(R.id.recycleviewEvents);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(0).getData(), context, MyEvents.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                Toast.makeText(MyEvents.this, "Events successfully loaded.", Toast.LENGTH_SHORT).show();

                nextBtn = (Button) findViewById(R.id.nextPageEvents);
                nextBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currPage[0] + 1 == pages.size()) {
                            Toast.makeText(MyEvents.this, "No next page.", Toast.LENGTH_SHORT).show();
                        } else {
                            currPage[0] += 1;
                            RecyclerView recyclerView = findViewById(R.id.recycleviewEvents);
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), context, MyEvents.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MyEvents.this));
                        }
                    }
                });
                backBtn = (Button) findViewById(R.id.backPageStud);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currPage[0] == 0) {
                            Toast.makeText(MyEvents.this, "No previous page.", Toast.LENGTH_SHORT).show();
                        } else {
                            currPage[0] -= 1;
                            RecyclerView recyclerView = findViewById(R.id.recycleviewEvents);
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(pages.get(currPage[0]).getData(), context, student.this);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(student.this));
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyEvents.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
            }
        };
        mirajDatabase.addValueEventListener(postListener);
    }
}
