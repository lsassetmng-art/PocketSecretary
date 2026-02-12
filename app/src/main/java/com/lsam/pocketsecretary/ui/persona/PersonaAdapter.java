package com.lsam.pocketsecretary.ui.persona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PersonaAdapter extends RecyclerView.Adapter<PersonaAdapter.VH> {

    private final Context context;
    private final PersonaSelectBottomSheet.OnPersonaSelected listener;
    private final List<String> personaIds = new ArrayList<>();

    public PersonaAdapter(Context context,
                          PersonaSelectBottomSheet.OnPersonaSelected listener) {

        this.context = context;
        this.listener = listener;

        try {
            String[] dirs = context.getAssets().list("persona");
            if (dirs != null) {
                personaIds.addAll(Arrays.asList(dirs));
            }
        } catch (IOException ignored) {}
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        String id = personaIds.get(position);

        Map<String, String> map =
                PersonaYamlLoader.load(context, id);

        String name = map.get("display_name");

        holder.text.setText(name != null ? name : id);

        holder.itemView.setOnClickListener(v -> {
            listener.onSelected(id);
        });
    }

    @Override
    public int getItemCount() {
        return personaIds.size();
    }

    static class VH extends RecyclerView.ViewHolder {

        TextView text;

        VH(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(android.R.id.text1);
        }
    }
}