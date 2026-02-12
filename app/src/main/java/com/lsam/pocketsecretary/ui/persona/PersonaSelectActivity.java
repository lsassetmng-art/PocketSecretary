package com.lsam.pocketsecretary.ui.persona;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;

public class PersonaSelectActivity extends AppCompatActivity
        implements PersonaSelectBottomSheet.OnPersonaSelected {

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_persona_select);

        RecyclerView recycler = findViewById(R.id.recyclerPersona);
        recycler.setLayoutManager(new LinearLayoutManager(this));

        recycler.setAdapter(
                new PersonaAdapter(this, this)
        );
    }

    @Override
    public void onSelected(String personaId) {

        CurrentPersonaStore.set(this, personaId);

        finish(); // 選択後に閉じる
    }
}