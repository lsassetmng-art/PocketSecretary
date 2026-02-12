package com.lsam.pocketsecretary.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.persona.CurrentPersonaStore;
import com.lsam.pocketsecretary.service.NotificationService;

import com.lsam.pocketsecretary.core.personaos.PersonaEngine;
import com.lsam.pocketsecretary.core.personaos.model.PersonaChannel;
import com.lsam.pocketsecretary.core.personaos.model.EmotionState;

public class ReminderWorker extends Worker {

    public ReminderWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {

        Context context = getApplicationContext();

        String message = getInputData().getString("message");
        if (message == null) {
            message = context.getString(R.string.reminder_default_message);
        }

        String personaId = CurrentPersonaStore.get(context);

        // Route through PersonaEngine (REMINDER). Keep original message as summary.
        String msg = PersonaEngine.generate(
                context,
                personaId,
                EmotionState.ALERT,
                PersonaChannel.REMINDER,
                message,
                null,
                null
        ).text;

        new NotificationService(context)
                .notifyAndRecord("worker", msg);

        return Result.success();
    }
}