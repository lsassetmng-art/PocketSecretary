package com.lsam.pocketsecretary.core.personaos.visual;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.lsam.pocketsecretary.core.persona.PersonaYamlLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class VisualComposeRepository {

    private static final String TAG = "VisualCompose";
    private static final ExecutorService EXEC =
            Executors.newSingleThreadExecutor();

    private VisualComposeRepository(){}

    public static void applyIfConfigured(
            Context ctx,
            String personaId,
            ImageView target
    ) {
        if (ctx == null || personaId == null || target == null) return;

        String jobId = extractJobId(ctx, personaId);
        if (jobId == null) return;

        Bitmap cached = tryLoadCache(ctx, jobId);
        if (cached != null) {
            target.setImageBitmap(cached);
            return;
        }

        EXEC.execute(() -> {
            try {

                VisualComposeResponse r =
                        VisualComposeClient.run(ctx, jobId);

                if (!r.success) {
                    Log.w(TAG, "compose failed: " + r.error);
                    return;
                }

                // ✅ 修正ポイント
                VisualComposeConfig cfg =
                        VisualComposeConfig.from(ctx);

                String url = StoragePublicUrl.build(
                        cfg.projectRef,
                        cfg.outputBucket,
                        r.outputPath
                );

                Bitmap bmp =
                        VisualImageDownloader.download(url);

                if (bmp == null) return;

                trySaveCache(ctx, jobId, bmp);

                target.post(() -> target.setImageBitmap(bmp));

            } catch (Throwable t) {
                Log.w(TAG, "compose error", t);
            }
        });
    }

    private static String extractJobId(
            Context ctx,
            String personaId
    ) {
        try {
            Map<String, String> yaml =
                    PersonaYamlLoader.load(ctx, personaId);

            if (yaml == null) return null;

            String id = yaml.get("visual_compose_job_id");
            if (id == null || id.isEmpty()) {
                id = yaml.get("job_id");
            }

            return id;

        } catch (Throwable ignored) {
            return null;
        }
    }

    private static File cacheFile(Context ctx, String jobId) {
        File dir = new File(ctx.getCacheDir(), "visual_compose");
        dir.mkdirs();
        return new File(dir, jobId + ".png");
    }

    private static Bitmap tryLoadCache(Context ctx, String jobId) {
        try {
            File f = cacheFile(ctx, jobId);
            if (!f.exists()) return null;
            return android.graphics.BitmapFactory.decodeFile(
                    f.getAbsolutePath()
            );
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static void trySaveCache(
            Context ctx,
            String jobId,
            Bitmap bmp
    ) {
        try {
            FileOutputStream os =
                    new FileOutputStream(cacheFile(ctx, jobId));
            bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
        } catch (Throwable ignored) {}
    }
}
