package com.lsam.pocketsecretary.ui.memo;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.memo.MemoDatabase;
import com.lsam.pocketsecretary.data.memo.MemoEntity;

public class MemoEditActivity extends BaseActivity {

    private EditText inputTitle;
    private EditText inputContent;

    private long editingId = -1L;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);

        setBaseContent(R.layout.activity_memo_edit);

        inputTitle = findViewById(R.id.inputTitle);
        inputContent = findViewById(R.id.inputContent);

        // 編集ID取得
        editingId = getIntent() != null
                ? getIntent().getLongExtra(
                        MemoListActivity.EXTRA_MEMO_ID,
                        -1L
                  )
                : -1L;

        if (editingId != -1L) {
            loadMemo(editingId);
        }

        Button btnSave = findViewById(R.id.btnSaveMemo);
        if (btnSave != null) {
            btnSave.setOnClickListener(v -> save());
        }
    }

    private void loadMemo(long id) {

        new Thread(() -> {

            MemoEntity entity =
                    MemoDatabase.get(getApplicationContext())
                            .memoDao()
                            .getById(id);

            if (entity == null) return;

            runOnUiThread(() -> {
                inputTitle.setText(entity.title != null ? entity.title : "");
                inputContent.setText(entity.content != null ? entity.content : "");
            });

        }).start();
    }

    private void save() {

        String title = inputTitle.getText() != null
                ? inputTitle.getText().toString().trim()
                : "";

        String content = inputContent.getText() != null
                ? inputContent.getText().toString().trim()
                : "";

        if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
            Toast.makeText(
                    this,
                    getString(R.string.ps_memo_input_required),
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        new Thread(() -> {

            try {

                MemoDatabase db =
                        MemoDatabase.get(getApplicationContext());

                MemoEntity entity;

                long now = System.currentTimeMillis();

                if (editingId != -1L) {

                    entity = db.memoDao().getById(editingId);

                    if (entity == null) {
                        entity = new MemoEntity();
                        entity.createdAt = now;
                    }

                } else {

                    entity = new MemoEntity();
                    entity.createdAt = now;
                }

                entity.title = title;
                entity.content = content;
                entity.updatedAt = now;

                db.memoDao().insert(entity);

                runOnUiThread(() -> {
                    Toast.makeText(
                            this,
                            getString(R.string.ps_memo_saved),
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                });

            } catch (Exception e) {

                runOnUiThread(() ->
                        Toast.makeText(
                                this,
                                e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show()
                );
            }

        }).start();
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
