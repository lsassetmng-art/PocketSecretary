package com.lsam.pocketsecretary.ui.event;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.event.EventEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.VH> {

    public interface Listener {
        void onClick(EventEntity e);
    }

    private final Listener listener;
    private final Context context;
    private List<EventEntity> items;

    private final SimpleDateFormat timeFmt = new SimpleDateFormat("HH:mm", Locale.getDefault());

    public EventAdapter(Context context, List<EventEntity> items, Listener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    public void update(List<EventEntity> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int position) {
        EventEntity e = items.get(position);

        // Accent bar color = theme accent
        h.accentBar.setBackgroundColor(resolveAccentColor(context));

        if (e.allDay) {
            h.time.setText(context.getString(R.string.event_all_day));
        } else {
            h.time.setText(timeFmt.format(new Date(e.startAt)));
        }

        h.title.setText(e.title == null ? "" : e.title);

        if (e.location == null || e.location.trim().isEmpty()) {
            h.location.setVisibility(View.GONE);
        } else {
            h.location.setVisibility(View.VISIBLE);
            h.location.setText(e.location);
        }

        // todo count will be injected by activity via tag (Integer) to avoid per-row DB calls here
        Object tag = h.itemView.getTag(R.id.tag_todo_count);
        if (tag instanceof Integer) {
            int c = (Integer) tag;
            if (c > 0) {
                h.todoCount.setVisibility(View.VISIBLE);
                h.todoCount.setText(c + " " + context.getString(R.string.event_todos_suffix));
            } else {
                h.todoCount.setVisibility(View.GONE);
            }
        } else {
            h.todoCount.setVisibility(View.GONE);
        }

        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(e); });
    }

    @Override public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        View accentBar;
        TextView time;
        TextView title;
        TextView location;
        TextView todoCount;

        VH(@NonNull View itemView) {
            super(itemView);
            accentBar = itemView.findViewById(R.id.accentBar);
            time = itemView.findViewById(R.id.textTime);
            title = itemView.findViewById(R.id.textTitle);
            location = itemView.findViewById(R.id.textLocation);
            todoCount = itemView.findViewById(R.id.textTodoCount);
        }
    }

    private static int resolveAccentColor(Context context) {
        TypedValue tv = new TypedValue();
        boolean ok = context.getTheme().resolveAttribute(android.R.attr.colorAccent, tv, true);
        if (!ok) {
            ok = context.getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimary, tv, true);
        }
        return ok ? tv.data : 0xFF1E3A8A; // fallback navy
    }
}
