package com.lsam.pocketsecretary.ui.onboarding;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.prefs.Prefs;

public class OnboardingActivity extends AppCompatActivity {

    private static final int REQ_CAL = 3001;
    private static final int REQ_NOTIF = 3002;

    private TextView txtState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        txtState = findViewById(R.id.txtOnboardingState);

        Button btnCal = findViewById(R.id.btnGrantCalendar);
        Button btnNotif = findViewById(R.id.btnGrantNotify);
        Button btnStart = findViewById(R.id.btnStartApp);

        btnCal.setOnClickListener(v -> requestCalendar());
        btnNotif.setOnClickListener(v -> requestNotify());
        btnStart.setOnClickListener(v -> {
            Prefs.setOnboarded(this, true);
            finish();
        });

        refreshState();
    }

    private void requestCalendar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR},
                    REQ_CAL);
        } else {
            refreshState();
        }
    }

    private void requestNotify() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQ_NOTIF);
            } else {
                refreshState();
            }
        } else {
            refreshState();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        refreshState();
    }

    private void refreshState() {
        boolean cal = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR)
                == PackageManager.PERMISSION_GRANTED;

        boolean notif = true;
        if (Build.VERSION.SDK_INT >= 33) {
            notif = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED;
        }

        txtState.setText(
                "隶難ｽｩ鬮ｯ蜊・・隲ｷ谺ｺn" +
                "郢晢ｽｻ郢ｧ・ｫ郢晢ｽｬ郢晢ｽｳ郢敖郢晢ｽｼ(READ): " + (cal ? "OK" : "隴幢ｽｪ髫ｪ・ｱ陷ｿ・ｯ") + "\n" +
                "郢晢ｽｻ鬨ｾ螟り｡・ " + (notif ? "OK" : "隴幢ｽｪ髫ｪ・ｱ陷ｿ・ｯ")
        );
    }
}
