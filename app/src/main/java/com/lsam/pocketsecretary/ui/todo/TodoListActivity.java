package com.lsam.pocketsecretary.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;

public class TodoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        TextView hint = new TextView(this);
        hint.setText(getString(R.string.ps_hint_stub));
        root.addView(hint, lp());

        Button btnAdd = new Button(this);
        btnAdd.setText(getString(R.string.ps_btn_add));
        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, TodoEditActivity.class))
        );
        root.addView(btnAdd, lp());

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
        return getString(R.string.ps_screen_todo);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}