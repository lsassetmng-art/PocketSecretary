package com.lsam.pocketsecretary.ui.todo;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.todo.TodoEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.VH> {

    public interface Listener {
        void onItemClick(TodoEntity e);
        void onItemLongPress(TodoEntity e);
        void onStatusToggle(TodoEntity e);
        void onSelectionChanged(int count);
    }

    private final Listener listener;
    private List<TodoEntity> items;

    private boolean selectionMode = false;
    private final Set<Long> selectedIds = new HashSet<>();

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

    public TodoAdapter(List<TodoEntity> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void update(List<TodoEntity> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    public boolean isSelectionMode() {
        return selectionMode;
    }

    public void exitSelectionMode() {
        selectionMode = false;
        selectedIds.clear();
        notifyDataSetChanged();
        if (listener != null) listener.onSelectionChanged(0);
    }

    public void enterSelectionMode(long firstId) {
        selectionMode = true;
        selectedIds.clear();
        selectedIds.add(firstId);
        notifyDataSetChanged();
        if (listener != null) listener.onSelectionChanged(selectedIds.size());
    }

    public Set<Long> getSelectedIds() {
        return new HashSet<>(selectedIds);
    }

    private void toggleSelection(long id) {
        if (selectedIds.contains(id)) selectedIds.remove(id);
        else selectedIds.add(id);
        notifyDataSetChanged();
        if (listener != null) listener.onSelectionChanged(selectedIds.size());
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        TodoEntity e = items.get(position);

        h.title.setText(e.title == null ? "" : e.title);

        boolean done = "done".equals(e.status);
        h.status.setChecked(done);

        if (done) {
            h.title.setPaintFlags(h.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            h.title.setAlpha(0.6f);
        } else {
            h.title.setPaintFlags(h.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            h.title.setAlpha(1.0f);
        }

        if (e.dueAt == null) {
            h.due.setText("");
        } else {
            h.due.setText(df.format(new Date(e.dueAt)));
        }

        if (e.eventId == null) {
            h.eventLink.setText("");
        } else {
            h.eventLink.setText("Linked");
        }

        if (selectionMode) {
            h.select.setVisibility(View.VISIBLE);
            h.select.setChecked(selectedIds.contains(e.id));
        } else {
            h.select.setVisibility(View.GONE);
        }

        h.status.setOnClickListener(v -> {
            if (listener != null) listener.onStatusToggle(e);
        });

        h.itemView.setOnClickListener(v -> {
            if (selectionMode) {
                toggleSelection(e.id);
            } else {
                if (listener != null) listener.onItemClick(e);
            }
        });

        h.itemView.setOnLongClickListener(v -> {
            if (!selectionMode) {
                if (listener != null) listener.onItemLongPress(e);
                return true;
            }
            return false;
        });

        h.select.setOnClickListener(v -> toggleSelection(e.id));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox status;
        TextView title;
        TextView due;
        TextView eventLink;
        CheckBox select;

        VH(@NonNull View itemView) {
            super(itemView);
            status = itemView.findViewById(R.id.checkboxStatus);
            title = itemView.findViewById(R.id.textTitle);
            due = itemView.findViewById(R.id.textDue);
            eventLink = itemView.findViewById(R.id.textEventLink);
            select = itemView.findViewById(R.id.checkboxSelect);
        }
    }
}
