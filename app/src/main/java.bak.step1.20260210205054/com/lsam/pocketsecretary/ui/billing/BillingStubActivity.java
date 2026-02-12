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
                "郢晢ｽｻ邵ｺ阮呻ｼ・ｸｺ・ｯ髫ｱ・ｲ鬩･螟ｧ・ｰ螳茨ｽｷ螢ｹ繝ｻ鬯ｪ・ｨ隴ｬ・ｼ邵ｺ・ｮ邵ｺ・ｿ\n" +
                "郢晢ｽｻ陞ｳ貅ｯ・ｪ・ｲ鬩･謇假ｽｼ繝ｻlay Billing繝ｻ蟲ｨ繝ｻ build.gradle 關捺剌・ｭ蛟ｩ・ｿ・ｽ陷会｣ｰ邵ｺ謔滂ｽｿ繝ｻ・ｦ窶貧" +
                "郢晢ｽｻUpgrade隴√・・ｨﾂ/FAQ邵ｺ・ｯ陟墓ｪ趣ｽｶ螢ｹ縲定淦・ｮ邵ｺ邇ｲ蟠帷ｸｺ繝ｻ
        );
    }
}
