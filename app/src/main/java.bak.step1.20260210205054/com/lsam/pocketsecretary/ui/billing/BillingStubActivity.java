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
                "驛｢譎｢・ｽ・ｻ驍ｵ・ｺ髦ｮ蜻ｻ・ｼ繝ｻ・ｸ・ｺ繝ｻ・ｯ鬮ｫ・ｱ繝ｻ・ｲ鬯ｩ・･陞滂ｽｧ繝ｻ・ｰ陞ｳ闌ｨ・ｽ・ｷ陞｢・ｹ郢晢ｽｻ鬯ｯ・ｪ繝ｻ・ｨ髫ｴ・ｬ繝ｻ・ｼ驍ｵ・ｺ繝ｻ・ｮ驍ｵ・ｺ繝ｻ・ｿ\n" +
                "驛｢譎｢・ｽ・ｻ髯橸ｽｳ雋・ｽｯ繝ｻ・ｪ繝ｻ・ｲ鬯ｩ・･隰・∞・ｽ・ｼ郢晢ｽｻlay Billing郢晢ｽｻ陝ｲ・ｨ郢晢ｽｻ build.gradle 髣懈瑳蜑後・・ｭ陋滂ｽｩ繝ｻ・ｿ繝ｻ・ｽ髯ｷ莨夲ｽ｣・ｰ驍ｵ・ｺ隰疲ｻゑｽｽ・ｿ郢晢ｽｻ繝ｻ・ｦ遯ｶ雋ｧ" +
                "驛｢譎｢・ｽ・ｻUpgrade髫ｴ竏壹・繝ｻ・ｨ・つ/FAQ驍ｵ・ｺ繝ｻ・ｯ髯溷｢難ｽｪ雜｣・ｽ・ｶ陞｢・ｹ邵ｲ螳壽ｷｦ繝ｻ・ｮ驍ｵ・ｺ驍・ｽｲ陝蟶ｷ・ｸ・ｺ郢晢ｽｻ
        );
    }
}
