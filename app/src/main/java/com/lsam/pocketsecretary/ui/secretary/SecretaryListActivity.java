package com.lsam.pocketsecretary.ui.secretary;

import android.os.Bundle;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;

public class SecretaryListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ BaseActivity統一方式
        setBaseContent(R.layout.activity_secretary_list);
    }

    @Override
    protected String getHeaderTitle() {
        return "Secretaries";
    }
}
