package com.example.loginstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class teacher extends AppCompatActivity implements RecyclerViewInterface {
    private Button exitBtn;
    private FloatingActionButton createEventBtn;
    private DatabaseReference mirajDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
        exitBtn = (Button) findViewById(R.id.quit);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(teacher.this, MainActivity.class));
            }
        });

        createEventBtn = (FloatingActionButton) findViewById(R.id.facultyCreate);
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(teacher.this, createEvent.class).putExtra("class", "teacher"));
            }
        });
        mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<DataSnapshot> childData= new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    childData.add(child);
                }
                RecyclerView recyclerView = findViewById(R.id.recycleviewfaculty);
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(childData, context, teacher.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                Toast.makeText(teacher.this, "Events successfully loaded.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(teacher.this, "Error. Try Again.", Toast.LENGTH_LONG).show();
            }
        };
        mirajDatabase.addValueEventListener(postListener);
    }

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickEdit(ArrayList<DataSnapshot> data, int position) {
        Intent edit = new Intent(this, editEvent.class);
        edit.putExtra("title", data.get(position).child("title").getValue(String.class));
        edit.putExtra("description", data.get(position).child("eventDescription").getValue(String.class));
        edit.putExtra("location", data.get(position).child("location").getValue(String.class));
        edit.putExtra("time", data.get(position).child("time").getValue(String.class));
        edit.putExtra("host", data.get(position).child("host").getValue(String.class));
        edit.putExtra("class", "teacher");
        startActivity(edit);
    }
}