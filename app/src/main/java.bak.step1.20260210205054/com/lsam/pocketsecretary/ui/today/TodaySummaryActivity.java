package com.lsam.pocketsecretary.ui.today;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.notification.UpcomingEventPicker;
import java.text.SimpleDateFormat;
import java.util.*;

public class TodaySummaryActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_today);
        TextView tv=findViewById(R.id.text);
        UpcomingEventPicker.Ev[] two=UpcomingEventPicker.pickTwo(this);
        StringBuilder sb=new StringBuilder("今日の予定\n");
        SimpleDateFormat f=new SimpleDateFormat("HH:mm",Locale.JAPAN);
        for(UpcomingEventPicker.Ev e: two){
            if(e!=null) sb.append(f.format(new Date(e.at))).append(" ").append(e.title).append("\n");
        }
        tv.setText(sb.toString());
    }
}
