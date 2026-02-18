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
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    public interface Listener {
        void onToggle(TodoEntity entity);
    }

    private List<TodoEntity> items;
    private final Listener listener;

    public TodoAdapter(List<TodoEntity> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void update(List<TodoEntity> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TodoEntity e = items.get(position);

        holder.title.setText(e.title);
        holder.content.setText(e.content == null ? "" : e.content);

        holder.checkBox.setChecked("done".equals(e.status));

        if ("done".equals(e.status)) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.title.setAlpha(0.5f);
        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.title.setAlpha(1f);
        }

        if (e.dueAt != null) {
            String formatted = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(new Date(e.dueAt));
            holder.due.setText(formatted);
            holder.due.setVisibility(View.VISIBLE);
        } else {
            holder.due.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(v -> {
            if (listener != null) listener.onToggle(e);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView title;
        TextView content;
        TextView due;

        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxStatus);
            title = itemView.findViewById(R.id.textTitle);
            content = itemView.findViewById(R.id.textContent);
            due = itemView.findViewById(R.id.textDue);
        }
    }
}
