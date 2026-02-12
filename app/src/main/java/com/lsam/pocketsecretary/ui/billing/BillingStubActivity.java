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
        note.setText(getString(R.string.billing_stub_note));
    }
}