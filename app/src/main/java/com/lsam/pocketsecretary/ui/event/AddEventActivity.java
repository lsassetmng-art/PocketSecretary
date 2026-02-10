package com.lsam.pocketsecretary.ui.event;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.event.SimpleEventStore;
import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_AT = "startAt";

    private long selectedAt;
    private String editId;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_add_event);

        EditText title = findViewById(R.id.edtEventTitle);
        Button pick = findViewById(R.id.btnPickTime);
        Button save = findViewById(R.id.btnSaveEvent);
        Button del  = findViewById(R.id.btnDeleteEvent);

        Calendar cal = Calendar.getInstance();
        selectedAt = cal.getTimeInMillis();

        if (getIntent().hasExtra(EXTRA_ID)) {
            editId = getIntent().getStringExtra(EXTRA_ID);
            title.setText(getIntent().getStringExtra(EXTRA_TITLE));
            selectedAt = getIntent().getLongExtra(EXTRA_AT, selectedAt);
            del.setVisibility(Button.VISIBLE);
        }

        pick.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this,(d,y,m,day)->{
                c.set(y,m,day);
                new TimePickerDialog(this,(t,h,min)->{
                    c.set(Calendar.HOUR_OF_DAY,h);
                    c.set(Calendar.MINUTE,min);
                    selectedAt = c.getTimeInMillis();
                    Toast.makeText(this,"譌･譎ゅｒ險ｭ螳壹＠縺ｾ縺励◆",Toast.LENGTH_SHORT).show();
                },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
            },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
        });

        save.setOnClickListener(v -> {
            String t = title.getText().toString().trim();
            if (t.isEmpty()) { Toast.makeText(this,"繧ｿ繧､繝医Ν蠢・・,Toast.LENGTH_SHORT).show(); return; }
            if (editId == null) SimpleEventStore.add(this,t,selectedAt);
            
            finish();
        });

        del.setOnClickListener(v -> {
            if (editId != null) SimpleEventStore.delete(this,editId);
            finish();
        });
    }
}
