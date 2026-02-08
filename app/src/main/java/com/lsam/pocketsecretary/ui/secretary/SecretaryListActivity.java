package com.lsam.pocketsecretary.ui.secretary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;

import java.util.ArrayList;
import java.util.List;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerSecretary);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SecretaryItem> items = new ArrayList<>();
        items.add(new SecretaryItem("ひより", "毎日をそっと支えます"));
        items.add(new SecretaryItem("あおい", "予定を静かに整理します"));
        items.add(new SecretaryItem("れん", "必要なことだけ伝えます"));

        recyclerView.setAdapter(new SecretaryListAdapter(items));
    }

    static class SecretaryItem {
        String name;
        String description;

        SecretaryItem(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }
}
