package com.lsam.pocketsecretary.ui.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.todo.TodoEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.VH> {

    public interface Listener {
        void onItemClick(TodoEntity e);
        void onItemLongPress(TodoEntity e);
        void onStatusToggle(TodoEntity e);
        void onSelectionChanged(int count);
    }

    private List<TodoEntity> items;
    private final Listener listener;

    private boolean selectionMode = false;
    private final Set<Long> selected = new HashSet<>();

    public TodoAdapter(List<TodoEntity> list, Listener l) {
        items = list;
        listener = l;
    }

    public void update(List<TodoEntity> list) {
        items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TodoEntity e = items.get(position);

        h.title.setText(e.title);

        h.check.setChecked("done".equals(e.status));

        h.check.setOnClickListener(v -> listener.onStatusToggle(e));

        h.itemView.setOnClickListener(v -> {
            if (selectionMode) {
                toggleSelect(e.id);
            } else {
                listener.onItemClick(e);
            }
        });

        h.itemView.setOnLongClickListener(v -> {
            listener.onItemLongPress(e);
            return true;
        });

        h.itemView.setActivated(selected.contains(e.id));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void toggleSelect(long id) {
        if (selected.contains(id)) {
            selected.remove(id);
        } else {
            selected.add(id);
        }
        listener.onSelectionChanged(selected.size());
        notifyDataSetChanged();
    }

    public void enterSelectionMode(long firstId) {
        selectionMode = true;
        selected.clear();
        selected.add(firstId);
        listener.onSelectionChanged(1);
        notifyDataSetChanged();
    }

    public void exitSelectionMode() {
        selectionMode = false;
        selected.clear();
        notifyDataSetChanged();
    }

    public Set<Long> getSelectedIds() {
        return selected;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        CheckBox check;

        VH(View v) {
            super(v);
            title = v.findViewById(R.id.textTitle);
            check = v.findViewById(R.id.checkboxStatus);
        }
    }
}
