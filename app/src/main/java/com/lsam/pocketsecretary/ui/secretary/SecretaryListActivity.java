package com.lsam.pocketsecretary.ui.secretary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerSecretaries);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SecretaryListAdapter(this));
    }
}