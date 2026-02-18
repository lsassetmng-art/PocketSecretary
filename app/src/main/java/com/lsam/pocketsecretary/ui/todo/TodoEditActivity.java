package com.lsam.pocketsecretary.ui.todo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.base.BaseActivity;
import com.lsam.pocketsecretary.data.todo.TodoEntity;
import com.lsam.pocketsecretary.data.todo.TodoRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class TodoEditActivity extends BaseActivity {

    public static final String EXTRA_TODO_ID = "todoId";

    private EditText editTitle;
    private EditText editContent;
    private Button btnPickDue;
    private TextView textEventValue;
    private Button btnUnlinkEvent;
    private Button btnMoveUp;
    private Button btnMoveDown;
    private Button btnSave;
    private Button btnDelete;

    private TodoRepository repo;

    private long todoId = -1L;
    private Long dueAt = null;
    private Long eventId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);
        applyThemeToRoot(findViewById(android.R.id.content));

        repo = new TodoRepository(this);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        btnPickDue = findViewById(R.id.btnPickDue);
        textEventValue = findViewById(R.id.textEventValue);
        btnUnlinkEvent = findViewById(R.id.btnUnlinkEvent);
        btnMoveUp = findViewById(R.id.btnMoveUp);
        btnMoveDown = findViewById(R.id.btnMoveDown);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        todoId = getIntent().getLongExtra(EXTRA_TODO_ID, -1L);
        long ev = getIntent().getLongExtra(TodoListActivity.EXTRA_EVENT_ID, -1L);
        if (ev > 0) eventId = ev;

        btnPickDue.setOnClickListener(v -> pickDate());
        btnUnlinkEvent.setOnClickListener(v -> {
            eventId = null;
            updateEventLabel();
        });

        btnMoveUp.setOnClickListener(v -> {
            if (todoId <= 0) return;
            repo.moveUp(todoId, new TodoRepository.Callback<Void>() {
                @Override public void onSuccess(Void value) { runOnUiThread(() -> toast("Moved")); }
                @Override public void onError(Exception e) { runOnUiThread(() -> toast("Move error")); }
            });
        });

        btnMoveDown.setOnClickListener(v -> {
            if (todoId <= 0) return;
            repo.moveDown(todoId, new TodoRepository.Callback<Void>() {
                @Override public void onSuccess(Void value) { runOnUiThread(() -> toast("Moved")); }
                @Override public void onError(Exception e) { runOnUiThread(() -> toast("Move error")); }
            });
        });

        btnSave.setOnClickListener(v -> save());
        btnDelete.setOnClickListener(v -> delete());

        if (todoId > 0) {
            load();
        } else {
            btnDelete.setEnabled(false);
            updateEventLabel();
        }
    }

    private void load() {
        repo.getById(todoId, new TodoRepository.Callback<TodoEntity>() {
            @Override
            public void onSuccess(TodoEntity value) {
                runOnUiThread(() -> {
                    if (value == null) {
                        toast("Not found");
                        finish();
                        return;
                    }
                    editTitle.setText(value.title == null ? "" : value.title);
                    editContent.setText(value.content == null ? "" : value.content);
                    dueAt = value.dueAt;
                    eventId = value.eventId;
                    updateDueLabel();
                    updateEventLabel();
                    btnDelete.setEnabled(true);
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> toast("Load error"));
            }
        });
    }

    private void save() {
        String title = editTitle.getText() == null ? "" : editTitle.getText().toString().trim();
        String content = editContent.getText() == null ? "" : editContent.getText().toString();

        if (title.isEmpty()) {
            toast("Title required");
            return;
        }

        if (todoId > 0) {
            repo.update(todoId, title, content, dueAt, eventId, new TodoRepository.Callback<Void>() {
                @Override public void onSuccess(Void value) { runOnUiThread(() -> { toast("Saved"); finish(); }); }
                @Override public void onError(Exception e) { runOnUiThread(() -> toast("Save error")); }
            });
        } else {
            repo.create(title, content, dueAt, eventId, new TodoRepository.Callback<Long>() {
                @Override public void onSuccess(Long value) { runOnUiThread(() -> { toast("Created"); finish(); }); }
                @Override public void onError(Exception e) { runOnUiThread(() -> toast("Create error")); }
            });
        }
    }

    private void delete() {
        if (todoId <= 0) return;
        List<Long> ids = new ArrayList<>();
        ids.add(todoId);
        repo.deleteByIds(ids, new TodoRepository.Callback<Void>() {
            @Override public void onSuccess(Void value) { runOnUiThread(() -> { toast("Deleted"); finish(); }); }
            @Override public void onError(Exception e) { runOnUiThread(() -> toast("Delete error")); }
        });
    }

    private void pickDate() {
        Calendar c = Calendar.getInstance();
        if (dueAt != null) c.setTimeInMillis(dueAt);

        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(Calendar.YEAR, year);
            picked.set(Calendar.MONTH, month);
            picked.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            picked.set(Calendar.HOUR_OF_DAY, 0);
            picked.set(Calendar.MINUTE, 0);
            picked.set(Calendar.SECOND, 0);
            picked.set(Calendar.MILLISECOND, 0);
            dueAt = picked.getTimeInMillis();
            updateDueLabel();
        }, y, m, d).show();
    }

    private void updateDueLabel() {
        if (dueAt == null) {
            btnPickDue.setText(getString(R.string.todo_due_pick));
        } else {
            btnPickDue.setText(String.valueOf(dueAt));
        }
    }

    private void updateEventLabel() {
        if (eventId == null) {
            textEventValue.setText(getString(R.string.todo_event_none));
        } else {
            textEventValue.setText("Linked");
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
