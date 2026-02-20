package com.lsam.pocketsecretary.ui.passport;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lsam.pocketsecretary.BaseActivity;
import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.personaos.passport.*;

import org.json.JSONObject;

public class PersonaPassportActivity extends BaseActivity {

    private TextView result;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setBaseContent(R.layout.activity_persona_passport);

        result = findViewById(R.id.passportResult);

        Button issueBtn = findViewById(R.id.btnIssuePassport);
        issueBtn.setOnClickListener(v -> issue());
    }

    private void issue() {

        new Thread(() -> {
            try {

                JSONObject payload = PersonaPayloadBuilder.create();
                JSONObject issueResp =
                        PersonaSnapshotClient.issue(null, payload);

                JSONObject snapshot = issueResp.getJSONObject("snapshot");

                JSONObject verifyResp =
                        PersonaSnapshotClient.verify(snapshot);

                if (verifyResp.optBoolean("valid")) {

                    PersonaSnapshotStore.save(this, snapshot);

                    runOnUiThread(() -> {
                        showSnapshot(snapshot);
                        Toast.makeText(this,
                                getString(R.string.ps_passport_success),
                                Toast.LENGTH_SHORT).show();
                    });

                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this,
                                    getString(R.string.ps_passport_invalid),
                                    Toast.LENGTH_LONG).show());
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                e.getMessage(),
                                Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void showSnapshot(JSONObject s) {
        result.setText(
                "Trust Level: " + s.optString("trust_level") + "\n" +
                "Nation Index: " + s.optDouble("nation_credit_index") + "\n" +
                "Issued: " + s.optString("issued_at")
        );
    }

    @Override
    protected String getHeaderTitle() {
        return getString(R.string.ps_passport_title);
    }

    @Override
    protected boolean showSettingsButton() {
        return false;
    }
}
