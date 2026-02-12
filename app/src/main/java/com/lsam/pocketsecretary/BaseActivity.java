package com.lsam.pocketsecretary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout contentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ベースレイアウトをセット
        super.setContentView(R.layout.activity_base);

        contentContainer = findViewById(R.id.baseContentContainer);

        // タイトル設定
        TextView title = findViewById(R.id.txtHeaderTitle);
        if (title != null) {
            title.setText(getHeaderTitle());
        }

        // 設定ボタン
        View settings = findViewById(R.id.headerSettings);
        if (settings != null) {
            settings.setOnClickListener(v ->
                    startActivity(new Intent(this, SettingsActivity.class))
            );
        }

        applyEdgePadding();
    }

    protected void setBaseContent(int layoutRes) {
        LayoutInflater.from(this)
                .inflate(layoutRes, contentContainer, true);
    }

    protected abstract String getHeaderTitle();

    protected void applyEdgePadding() {
        View header = findViewById(R.id.commonHeaderRoot);
        if (header != null) {
            header.setPadding(
                    header.getPaddingLeft(),
                    header.getPaddingTop() + getStatusBarHeight(),
                    header.getPaddingRight(),
                    header.getPaddingBottom()
            );
        }
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier(
                "status_bar_height",
                "dimen",
                "android"
        );
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
