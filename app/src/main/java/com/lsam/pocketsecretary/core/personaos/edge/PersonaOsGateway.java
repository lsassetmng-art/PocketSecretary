package com.lsam.pocketsecretary.core.personaos.edge;

import android.content.Context;

import com.lsam.pocketsecretary.core.EdgeApiClient;
import com.lsam.pocketsecretary.core.SupabaseManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import java.io.IOException;

public class PersonaOsGateway {

    public static void onDashboardOpened(
            Context context,
            String personaId
    ) {

        try {

            // 🔐 既に匿名ログイン済み前提（Applicationで実施）
            String accessToken = SupabaseManager.getAccessToken();
            if (accessToken == null || accessToken.isEmpty()) {
                return;
            }

            // 🔥 Edge呼び出し
            EdgeApiClient.applyPersonaEvent(
                    accessToken,
                    personaId,
                    "BUSINESS_SECRETARY_TOOL_USED",
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.body() != null) {
                                String result = response.body().string();
                                System.out.println(result);
                            }
                        }
                    }
            );

        } catch (Throwable ignored) {}
    }
}
