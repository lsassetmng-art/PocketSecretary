package com.lsam.pocketsecretary.ui.secretary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;

/**
 * Phase C: UI only
 */
public class SecretaryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_list);
        setTitle(R.string.secretary_list_title);
    }
}