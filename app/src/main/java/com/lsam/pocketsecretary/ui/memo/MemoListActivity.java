package com.lsam.pocketsecretary.ui.memo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.memo.MemoDatabase;
import com.lsam.pocketsecretary.data.memo.MemoEntity;

import java.util.ArrayList;
import java.util.List;

public class MemoListActivity extends BaseActivity {

    public static final String EXTRA_MEMO_ID = "extra_memo_id";

    private MemoAdapter adapter;
    private final List<MemoEntity> current = new ArrayList<>();
    private TextView emptyView;
    private EditText searchInput;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_memo_list);

        emptyView = findViewById(R.id.emptyView);
        searchInput = findViewById(R.id.searchInput);

        RecyclerView rv = findViewById(R.id.memoRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MemoAdapter(current, m -> openEdit(m.id));
        rv.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddMemo);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, MemoEditActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMemos();
    }

    private void loadMemos() {

        new Thread(() -> {

            List<MemoEntity> list =
                    MemoDatabase.get(getApplicationContext())
                            .memoDao()
                            .findAll();

            runOnUiThread(() -> {

                current.clear();
                if (list != null) current.addAll(list);

                adapter.notifyDataSetChanged();

                emptyView.setVisibility(
                        current.isEmpty() ? View.VISIBLE : View.GONE
                );
            });

        }).start();
    }

    private void openEdit(long memoId) {
        Intent intent = new Intent(this, MemoEditActivity.class);
        intent.putExtra(EXTRA_MEMO_ID, memoId);
        startActivity(intent);
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_screen_memo);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
