package com.lsam.pocketsecretary.ui.secretary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.model.SecretaryItem;
import java.util.List;

public class SecretaryListAdapter
    extends RecyclerView.Adapter<SecretaryListAdapter.VH> {

    public interface OnItemClickListener {
        void onClick(SecretaryItem item);
    }

    private final List<SecretaryItem> items;
    private final OnItemClickListener listener;

    public SecretaryListAdapter(List<SecretaryItem> items, OnItemClickListener l) {
        this.items = items;
        this.listener = l;
    }

    @Override public VH onCreateViewHolder(ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext())
            .inflate(R.layout.row_secretary, p, false);
        return new VH(view);
    }

    @Override public void onBindViewHolder(VH h, int i) {
        SecretaryItem it = items.get(i);
        h.name.setText(it.name);
        h.itemView.setOnClickListener(v -> listener.onClick(it));
    }

    @Override public int getItemCount() { return items.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView name;
        VH(View v) {
            super(v);
            name = v.findViewById(R.id.secretary_name);
        }
    }
}
