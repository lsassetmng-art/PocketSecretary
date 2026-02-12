package com.lsam.pocketsecretary.ui.week;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import com.lsam.pocketsecretary.core.repeat.RepeatUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.*;

public class WeeklyReadActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_weekly_read);

        ListView lv = findViewById(R.id.listWeek);
        List<String> rows = new ArrayList<>();

        long now = System.currentTimeMillis();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY,0); start.set(Calendar.MINUTE,0); start.set(Calendar.SECOND,0);
        Calendar end = (Calendar) start.clone(); end.add(Calendar.DATE,7);

        SimpleDateFormat df = new SimpleDateFormat("E HH:mm", Locale.getDefault());

        // ---- external calendar ----
        Cursor c = getContentResolver().query(
            CalendarContract.Events.CONTENT_URI,
            new String[]{CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART},
            CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + " < ?",
            new String[]{String.valueOf(start.getTimeInMillis()), String.valueOf(end.getTimeInMillis())},
            CalendarContract.Events.DTSTART + " ASC"
        );
        if (c!=null){
            try{
                while(c.moveToNext()){
                    String t=c.getString(0);
                    long at=c.getLong(1);
                    rows.add(df.format(new Date(at))+"  "+t);
                }
            } finally { c.close(); }
        }

        // ---- internal simple events (repeat-aware) ----
        try{
            JSONArray arr = SimpleEventStore.list(this);
            for (int i=0;i<arr.length();i++){
                JSONObject o = arr.getJSONObject(i);
                String title=o.getString("title");
                long base=o.getLong("startAt");
                String rep=o.optString("repeat",null);

                long at=base;
                for(int d=0; d<7; d++){
                    if (rep!=null && d>0) at = RepeatUtil.nextAt(at, rep);
                    if (at>=start.getTimeInMillis() && at<end.getTimeInMillis()){
                        rows.add("・滓ｨ抵ｽｵ・ｱ "+df.format(new Date(at))+"  "+title);
                    }
                }
            }
        }catch(Exception ignored){}

        if (rows.isEmpty()){
            rows.add("髣費｣ｰ闔・･繝ｻ・ｮ陞｢・ｹ郢晢ｽｻ驍ｵ・ｺ郢ｧ繝ｻ・ｽ鬘費ｽｸ・ｺ繝ｻ・ｾ驍ｵ・ｺ陝ｶ蜻ｻ・ｽ骰具ｽｸ・ｲ郢ｧ謇假ｽｽ・ｼ陋滂ｽｶ繝ｻ・ｺ闔・･繝ｻ・ｮ陞｢・ｹ・ゑｽｰ驛｢・ｧ髣・ｽｽ繝ｻ・ｿ繝ｻ・ｽ髯ｷ莨夲ｽ｣・ｰ驍ｵ・ｺ繝ｻ・ｧ驍ｵ・ｺ鬮ｦ・ｪ遶擾ｽｪ驍ｵ・ｺ陷ｷ・ｶ・つ郢晢ｽｻ);
        }

        lv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows));
    }
}
