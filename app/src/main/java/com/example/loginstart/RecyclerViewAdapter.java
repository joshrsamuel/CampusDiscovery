package com.example.loginstart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<DataSnapshot> data;
    Context context;
    public RecyclerViewAdapter(ArrayList<DataSnapshot> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.event_cell, parent, false);

        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).child("title").getValue(String.class));
        System.out.println(data.get(position).child("title").getValue(String.class));
        holder.date.setText(data.get(position).child("time").getValue(String.class));
        holder.location.setText(data.get(position).child("location").getValue(String.class));
        holder.description.setText(data.get(position).child("eventDescription").getValue(String.class));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, date, location, description;
        Button removeEvent;
        DatabaseReference mirajDatabase = FirebaseDatabase.getInstance("https://campusdiscovery-d2e9f-default-rtdb.firebaseio.com/").getReference("Events");

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById((R.id.eventDate));
            location = itemView.findViewById(R.id.eventLocation);
            description = itemView.findViewById((R.id.eventDes));

            removeEvent = itemView.findViewById(R.id.removeEvent);
            removeEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mirajDatabase.child(title.getText().toString()).removeValue();
                }
            });
        }
    }
}
