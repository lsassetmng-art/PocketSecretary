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
                "繝ｻ縺薙％縺ｯ隱ｲ驥大ｰ守ｷ壹・鬪ｨ譬ｼ縺ｮ縺ｿ\n" +
                "繝ｻ螳溯ｪｲ驥托ｼ・lay Billing・峨・ build.gradle 萓晏ｭ倩ｿｽ蜉縺悟ｿ・ｦ―n" +
                "繝ｻUpgrade譁・ｨ/FAQ縺ｯ蠕檎ｶ壹〒蟾ｮ縺玲崛縺・
        );
    }
}
