package com.example.loginstart;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecyclerViewAdapter3 extends RecyclerView.Adapter<RecyclerViewAdapter3.MyViewHolder> {

    List<DataSnapshot> data;
    Context context;
    DatabaseReference userDatabase;
    DatabaseReference eventDatabase;
    userInfo eventHost;
    public RecyclerViewAdapter3(List<DataSnapshot> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter3.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_cell, parent, false);

        return new RecyclerViewAdapter3.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).child("title").getValue(String.class));
        holder.date.setText(data.get(position).child("time").getValue(String.class));
        holder.location.setText(data.get(position).child("location").getValue(String.class));
        holder.description.setText(data.get(position).child("eventDescription").getValue(String.class));
        userDatabase = FirebaseDatabase.getInstance().getReference("UserInfo");
        userDatabase.child(data.get(position).child("host").getValue(String.class)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventHost = snapshot.getValue(userInfo.class);
                if (eventHost !=null) {
                    holder.host.setText(eventHost.getFullName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed");
            }
        });
        holder.editEvent.setVisibility(View.GONE);
        holder.removeEvent.setVisibility((View.GONE));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, location, description, host;
        FirebaseUser currUser;
        Button removeEvent;
        Button editEvent;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById((R.id.eventDate));
            location = itemView.findViewById(R.id.eventLocation);
            description = itemView.findViewById((R.id.eventDes));
            host = itemView.findViewById(R.id.eventHost);

            currUser = FirebaseAuth.getInstance().getCurrentUser();

            //Edit Button
            editEvent = itemView.findViewById(R.id.editEvent);

            // Remove Button
            removeEvent = itemView.findViewById(R.id.removeEvent);
        }
    }
}

