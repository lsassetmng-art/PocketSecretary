package com.lsam.pocketsecretary.ui.persona;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.core.settings.StageStore;

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

        TextView stageInfo = v.findViewById(R.id.txtStageInfo);
        View btnStageS0 = v.findViewById(R.id.btnStageS0);
        View btnStageS1 = v.findViewById(R.id.btnStageS1);

        String personaId = CurrentPersonaStore.get(getContext());
        if (personaId == null || personaId.isEmpty()) personaId = "alpha";

        String stage = StageStore.getStage(getContext(), personaId);
        if (stage == null || stage.isEmpty()) stage = "s0";

        if (stageInfo != null) {
            stageInfo.setText("Stage: " + stage);
        }

        final String finalPersonaId = personaId;

        if (btnStageS0 != null) {
            btnStageS0.setOnClickListener(view -> {
                StageStore.setStage(getContext(), finalPersonaId, "s0");
                if (listener != null) listener.onSelected(finalPersonaId);
                dismissAllowingStateLoss();
            });
        }

        if (btnStageS1 != null) {
            btnStageS1.setOnClickListener(view -> {
                StageStore.setStage(getContext(), finalPersonaId, "s1");
                if (listener != null) listener.onSelected(finalPersonaId);
                dismissAllowingStateLoss();
            });
        }

        RecyclerView recycler = v.findViewById(R.id.recyclerPersona);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new PersonaAdapter(getContext(), listener));

        return v;
    }
}
