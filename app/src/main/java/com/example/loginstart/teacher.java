package com.example.loginstart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class teacher extends AppCompatActivity {
    private Button exitBtn;
    private FloatingActionButton createEventBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            ArrayList<DataSnapshot> childData= new ArrayList<>();
            for (DataSnapshot child : snapshot.getChildren()) {
                childData.add(child);
            }
            new RecyclerViewAdapter(childData);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    private void exitApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}