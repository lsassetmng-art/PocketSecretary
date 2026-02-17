package com.lsam.pocketsecretary.ui.request;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;

public class RequestHubActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        TextView hint = new TextView(this);
        hint.setText(getString(R.string.ps_hint_stub));
        root.addView(hint, lp());

        setContentView(root);
    }

    private static LinearLayout.LayoutParams lp() {
        return new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_screen_request);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}