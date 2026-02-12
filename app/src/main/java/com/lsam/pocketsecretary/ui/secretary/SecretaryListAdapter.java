package com.lsam.pocketsecretary.ui.secretary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SecretaryListAdapter extends RecyclerView.Adapter<SecretaryListAdapter.VH> {

    public SecretaryListAdapter(Context context) {}

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = new View(parent.getContext());
        v.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {}

    @Override
    public int getItemCount() {
        return 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
