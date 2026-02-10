package com.lsam.pocketsecretary.ui.billing;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lsam.pocketsecretary.R;

public class BillingStubActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_stub);

        TextView note = findViewById(R.id.txtBillingNote);
        note.setText(
                "Billing (Stub UI)\n\n" +
                "・ここは課金導線の骨格のみ\n" +
                "・実課金（Play Billing）は build.gradle 依存追加が必要\n" +
                "・Upgrade文言/FAQは後続で差し替え"
        );
    }
}
