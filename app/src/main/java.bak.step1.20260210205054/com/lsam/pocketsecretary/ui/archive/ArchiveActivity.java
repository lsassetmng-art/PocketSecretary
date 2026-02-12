package com.lsam.pocketsecretary.ui.archive;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.archive.DocumentStore;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {
    private static final int REQ = 7001;
    private ArrayAdapter<String> adapter;
    private final ArrayList<String> rows = new ArrayList<>();

    @Override protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_archive);

        ListView lv = findViewById(R.id.listDocs);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, rows);
        lv.setAdapter(adapter);

        Button add = findViewById(R.id.btnAddDoc);
        add.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            startActivityForResult(i, REQ);
        });

        reload();
    }

    private void reload(){
        rows.clear();
        JSONArray arr = DocumentStore.list(this);
        for (int i=0;i<arr.length();i++){
            try{
                JSONObject o = arr.getJSONObject(i);
                rows.add("[" + o.optString("category","驍ｵ・ｺ隴擾ｽｴ郢晢ｽｻ髣泌ｳｨ繝ｻ) + "] " + o.optString("title","(no title)"));
            }catch(Exception ignored){}
        }
        adapter.notifyDataSetChanged();
    }

    @Override protected void onActivityResult(int req, int res, Intent data){
        super.onActivityResult(req,res,data);
        if (req==REQ && res==RESULT_OK && data!=null){
            Uri uri = data.getData();
            if (uri==null) return;
            try{
                getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }catch(Exception ignored){}

            String name = "髫ｴ蜴・ｽｽ・ｸ鬯ｯ蛟･繝ｻ;
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            if (c!=null){
                try{
                    int idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (c.moveToFirst() && idx>=0) name = c.getString(idx);
                } finally { c.close(); }
            }
            DocumentStore.add(this, name, "驍ｵ・ｺ隴擾ｽｴ郢晢ｽｻ髣泌ｳｨ繝ｻ, "", uri);
            reload();
        }
    }
}
