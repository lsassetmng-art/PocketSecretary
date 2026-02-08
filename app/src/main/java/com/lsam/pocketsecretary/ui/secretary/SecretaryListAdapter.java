package com.lsam.pocketsecretary.ui.secretary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;

import java.util.List;

public class SecretaryListAdapter extends RecyclerView.Adapter<SecretaryListAdapter.ViewHolder> {

    private final List<SecretaryListActivity.SecretaryItem> items;

    public SecretaryListAdapter(List<SecretaryListActivity.SecretaryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_secretary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SecretaryListActivity.SecretaryItem item = items.get(position);
        holder.txtName.setText(item.name);
        holder.txtDescription.setText(item.description);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtDescription;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtSecretaryName);
            txtDescription = itemView.findViewById(R.id.txtSecretaryDescription);
        }
    }
}
