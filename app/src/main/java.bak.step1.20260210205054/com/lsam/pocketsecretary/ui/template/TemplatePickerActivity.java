package com.lsam.pocketsecretary.ui.template;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.core.template.EventTemplates;
import com.lsam.pocketsecretary.core.template.TemplateUsageStore;
import java.util.*;
import java.util.Calendar;

public class TemplatePickerActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_template_picker);

        ListView lv = findViewById(R.id.listTemplates);
        List<EventTemplates.T> ts = TemplateUsageStore.sorted(this);

        List<String> rows = new ArrayList<>();
        for (EventTemplates.T t: ts){
            rows.add(t.title + "・・ + t.hour + ":00・・);
        }

        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
        lv.setOnItemClickListener((p,v,pos,id)->{
            EventTemplates.T t = ts.get(pos);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, t.hour);
            c.set(Calendar.MINUTE, 0);
            SimpleEventStore.add(this, t.title, c.getTimeInMillis(), null);
            TemplateUsageStore.bump(this, t.title);
            finish();
        });
    }
}
