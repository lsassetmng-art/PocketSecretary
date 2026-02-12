package com.lsam.pocketsecretary.ui.persona;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lsam.pocketsecretary.R;

public class PersonaSelectBottomSheet extends BottomSheetDialogFragment {

    public interface OnPersonaSelected {
        void onSelected(String personaId);
    }

    private final OnPersonaSelected listener;

    public PersonaSelectBottomSheet(OnPersonaSelected l) {
        this.listener = l;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sheet_persona_select, container, false);

        RecyclerView recycler = v.findViewById(R.id.recyclerPersona);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new PersonaAdapter(getContext(), listener));

        return v;
    }
}