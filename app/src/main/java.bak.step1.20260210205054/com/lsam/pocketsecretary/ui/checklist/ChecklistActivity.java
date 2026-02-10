package com.lsam.pocketsecretary.ui.checklist;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.checklist.ChecklistStore;
import org.json.*;

public class ChecklistActivity extends AppCompatActivity {
    ArrayAdapter<String> ad;
    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_checklist);
        ad=new ArrayAdapter<>(this,android.R.layout.simple_list_item_multiple_choice);
        ListView lv=findViewById(R.id.list);
        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lv.setAdapter(ad);
        findViewById(R.id.btnAdd).setOnClickListener(v->{
            EditText e=findViewById(R.id.edit);
            if(e.getText().length()>0){
                ChecklistStore.add(this,e.getText().toString());
                e.setText(""); reload(lv);
            }
        });
        lv.setOnItemClickListener((p,v,i,id)->{
            ChecklistStore.toggle(this,i);
            reload(lv);
        });
        reload(lv);
    }
    void reload(ListView lv){
        ad.clear();
        JSONArray a=ChecklistStore.list(this);
        for(int i=0;i<a.length();i++){
            try{
                JSONObject o=a.getJSONObject(i);
                ad.add(o.getString("text"));
                lv.setItemChecked(i,o.getBoolean("done"));
            }catch(Exception ignored){}
        }
    }
}
