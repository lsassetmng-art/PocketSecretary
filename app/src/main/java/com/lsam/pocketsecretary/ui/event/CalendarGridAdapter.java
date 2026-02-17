package com.lsam.pocketsecretary.ui.event;

import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalendarGridAdapter
        extends RecyclerView.Adapter<CalendarGridAdapter.Holder> {

    public interface OnCellClick {
        void onClick(CalendarCellModel cell);
    }

    private List<CalendarCellModel> data;
    private OnCellClick listener;

    public CalendarGridAdapter(
            List<CalendarCellModel> data,
            OnCellClick listener
    ) {
        this.data = data;
        this.listener = listener;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        Button btn = new Button(parent.getContext());
        return new Holder(btn);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        CalendarCellModel cell = data.get(position);
        holder.btn.setText(String.valueOf(cell.day));

        if (cell.hasEvent) {
            holder.btn.setText(cell.day + " ●");
        }

        holder.btn.setOnClickListener(v -> {
            if (listener != null) listener.onClick(cell);
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        Button btn;
        Holder(Button b) {
            super(b);
            btn = b;
        }
    }
}