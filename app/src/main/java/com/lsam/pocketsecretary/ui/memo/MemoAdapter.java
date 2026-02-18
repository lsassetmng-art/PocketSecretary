package com.lsam.pocketsecretary.ui.memo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.data.memo.MemoEntity;

import java.util.List;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(MemoEntity memo);
    }

    private final List<MemoEntity> items;
    private final OnItemClickListener listener;

    public MemoAdapter(List<MemoEntity> items,
                       OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1,
                        parent,
                        false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        MemoEntity memo = items.get(position);

        String title = memo.title != null && !memo.title.isEmpty()
                ? memo.title
                : "(no title)";

        holder.textView.setText(title);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(memo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
