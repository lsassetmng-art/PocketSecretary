package com.lsam.pocketsecretary.ui.archive;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;

/**
 * Phase D: Archive READ UI only
 */
public class ArchiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);
        setTitle(R.string.archive_title);
    }
}