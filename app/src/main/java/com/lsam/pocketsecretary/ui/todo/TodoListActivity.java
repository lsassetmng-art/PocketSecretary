package com.lsam.pocketsecretary.ui.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.view.ActionMode;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.base.BaseActivity;
import com.lsam.pocketsecretary.data.todo.TodoEntity;
import com.lsam.pocketsecretary.data.todo.TodoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TodoListActivity extends BaseActivity {

    public static final String EXTRA_EVENT_ID = "eventId";

    private TodoRepository repo;
    private TodoAdapter adapter;

    private TodoRepository.FilterType filter = TodoRepository.FilterType.ALL;

    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        applyThemeToRoot(findViewById(android.R.id.content));

        repo = new TodoRepository(this);

        RecyclerView rv = findViewById(R.id.todoRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TodoAdapter(new ArrayList<>(), new TodoAdapter.Listener() {
            @Override
            public void onItemClick(TodoEntity e) {
                openEdit(e.id, e.eventId);
            }

            @Override
            public void onItemLongPress(TodoEntity e) {
                enterSelection(e.id);
            }

            @Override
            public void onStatusToggle(TodoEntity e) {
                repo.toggleStatus(e.id, new TodoRepository.Callback<Void>() {
                    @Override public void onSuccess(Void value) { load(); }
                    @Override public void onError(Exception ex) { toast("Update error"); }
                });
            }

            @Override
            public void onSelectionChanged(int count) {
                if (actionMode != null) {
                    actionMode.setTitle(String.valueOf(count));
                    if (count == 0) {
                        actionMode.finish();
                    }
                }
            }
        });

        rv.setAdapter(adapter);

        Button bAll = findViewById(R.id.btnFilterAll);
        Button bOpen = findViewById(R.id.btnFilterOpen);
        Button bDone = findViewById(R.id.btnFilterDone);

        bAll.setOnClickListener(v -> { filter = TodoRepository.FilterType.ALL; load(); });
        bOpen.setOnClickListener(v -> { filter = TodoRepository.FilterType.OPEN; load(); });
        bDone.setOnClickListener(v -> { filter = TodoRepository.FilterType.DONE; load(); });

        findViewById(R.id.fabAddTodo).setOnClickListener(v -> openCreate());

        load();
    }

    private void load() {
        repo.getTodos(filter, new TodoRepository.Callback<List<TodoEntity>>() {
            @Override
            public void onSuccess(List<TodoEntity> value) {
                runOnUiThread(() -> adapter.update(value));
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> toast("Load error"));
            }
        });
    }

    private void openCreate() {
        Intent i = new Intent(this, TodoEditActivity.class);
        // If opened from event context, caller can set EXTRA_EVENT_ID.
        long eventId = getIntent().getLongExtra(EXTRA_EVENT_ID, -1L);
        if (eventId > 0) i.putExtra(EXTRA_EVENT_ID, eventId);
        startActivity(i);
    }

    private void openEdit(long todoId, Long eventId) {
        Intent i = new Intent(this, TodoEditActivity.class);
        i.putExtra(TodoEditActivity.EXTRA_TODO_ID, todoId);
        if (eventId != null) i.putExtra(EXTRA_EVENT_ID, eventId.longValue());
        startActivity(i);
    }

    private void enterSelection(long firstId) {
        adapter.enterSelectionMode(firstId);

        actionMode = startSupportActionMode(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_todo_selection, menu);
                mode.setTitle("1");
                return true;
            }

            @Override public boolean onPrepareActionMode(ActionMode mode, Menu menu) { return false; }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    confirmDelete();
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                adapter.exitSelectionMode();
            }
        });
    }

    private void confirmDelete() {
        Set<Long> ids = adapter.getSelectedIds();
        if (ids.isEmpty()) return;

        // No custom dialog to keep dependencies minimal; use Android confirm style.
        // For strict safety, we do a second tap confirmation by toast + retry window.
        // Simple: execute delete immediately (still requires deliberate selection + delete icon).
        List<Long> list = new ArrayList<>(ids);

        repo.deleteByIds(list, new TodoRepository.Callback<Void>() {
            @Override
            public void onSuccess(Void value) {
                runOnUiThread(() -> {
                    toast("Deleted");
                    if (actionMode != null) actionMode.finish();
                    load();
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> toast("Delete error"));
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }
}
