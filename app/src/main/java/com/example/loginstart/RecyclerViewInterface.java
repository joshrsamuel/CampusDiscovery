package com.example.loginstart;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

public interface RecyclerViewInterface {
    void onClickEdit(ArrayList<DataSnapshot> data, int position);
}
