package com.example.loginstart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private ArrayList<DataSnapshot> data;
    private Context context;
    public RecyclerViewAdapter(ArrayList<DataSnapshot> data, Context context) {
        this.data = data;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row, parent, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).child("title").getValue(String.class));
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

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.eventTitle);
            date = itemView.findViewById((R.id.eventDate));
            location = itemView.findViewById(R.id.eventLocation);
            description = itemView.findViewById((R.id.eventDes));
        }
    }
}
