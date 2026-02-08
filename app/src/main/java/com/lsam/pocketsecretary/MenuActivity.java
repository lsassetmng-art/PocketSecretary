package com.lsam.pocketsecretary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        findViewById(R.id.btnSecretary).setOnClickListener(v ->
            startActivity(new Intent(this, SecretaryActivity.class))
        );
    }
}