package com.lsam.pocketsecretary.ui.billing;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.lsam.pocketsecretary.R;

public class PremiumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle b){
        super.onCreate(b);
        setContentView(R.layout.activity_premium);

        TextView t = findViewById(R.id.txtPremiumInfo);
        t.setText(getString(R.string.premium_stub_text));
    }
}
