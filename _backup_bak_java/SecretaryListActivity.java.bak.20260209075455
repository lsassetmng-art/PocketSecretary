package com.lsam.pocketsecretary.ui.secretary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.ui.billing.BillingStubActivity;
import com.lsam.pocketsecretary.ui.calendar.CalendarReadActivity;
import com.lsam.pocketsecretary.ui.notification.NotificationCenterActivity;

import java.util.ArrayList;
import java.util.List;

public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_list);

        Button btnCalendar = findViewById(R.id.btnCalendar);
        Button btnNotify = findViewById(R.id.btnNotify);
        Button btnBilling = findViewById(R.id.btnBilling);

        btnCalendar.setOnClickListener(v -> startActivity(new Intent(this, CalendarReadActivity.class)));
        btnNotify.setOnClickListener(v -> startActivity(new Intent(this, NotificationCenterActivity.class)));
        btnBilling.setOnClickListener(v -> startActivity(new Intent(this, BillingStubActivity.class)));

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
