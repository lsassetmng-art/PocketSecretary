package com.lsam.pocketsecretary.core.personaos.provider;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import com.lsam.pocketsecretary.R;
import com.lsam.pocketsecretary.core.personaos.model.*;
import com.lsam.pocketsecretary.core.personaos.util.TextUtil;
import com.lsam.pocketsecretary.core.personaos.util.VersionGate;

/**
 * Phase F local provider: safe, resource-only texts, version gate, reason codes.
 */
public final class LocalPersonaProvider implements PersonaProvider {

    private final Context app;
    private final String personaVersion;
    private final boolean backwardCompatible;

    public LocalPersonaProvider(Context context) {
        this(context, "0.5.0", true);
    }

    public LocalPersonaProvider(Context context, String personaVersion, boolean backwardCompatible) {
        this.app = context.getApplicationContext();
        this.personaVersion = personaVersion;
        this.backwardCompatible = backwardCompatible;
    }

    @Override
    public String providerName() { return "LocalPersonaProvider"; }

    @Override
    public PersonaResponse generate(PersonaRequest req) {
        PersonaContext c = req.context;
        PersonaRequest.Constraints cons = (req.constraints != null) ? req.constraints : PersonaRequest.Constraints.defaults();

        List<String> reasons = new ArrayList<>();
        reasons.add(channelReason(c.channel));
        reasons.add(emotionReason(c.emotionState));
        if (c.upcomingEventSummary != null && !c.upcomingEventSummary.trim().isEmpty()) reasons.add(ReasonCodes.HAS_UPCOMING_SUMMARY);
        if (c.todayEventCount != null) reasons.add(ReasonCodes.HAS_TODAY_COUNT);

        boolean ok = VersionGate.isAcceptable(personaVersion, backwardCompatible);
        reasons.add(ok ? ReasonCodes.VERSION_ACCEPTED : ReasonCodes.VERSION_REJECTED);

        PersonaToneTag tone = toneByChannel(c.channel);

        String text;
        if (!ok) {
            text = app.getString(R.string.persona_error_incompatible);
        } else {
            text = buildText(c);
        }

        text = TextUtil.trimTo(text, cons.maxChars);

        return new PersonaResponse(
                c.requestId,
                c.personaId,
                text,
                tone,
                reasons,
                personaVersion,
                backwardCompatible
        );
    }

    private String channelReason(PersonaChannel ch) {
        switch (ch) {
            case MORNING: return ReasonCodes.MORNING_BRIEF;
            case UPCOMING: return ReasonCodes.UPCOMING_EVENT;
            case REMINDER: return ReasonCodes.REMINDER;
            case SPEECH_TOOL: return ReasonCodes.SPEECH_TOOL;
            case MANUAL: return ReasonCodes.MANUAL;
            default: return ReasonCodes.DASHBOARD;
        }
    }

    private String emotionReason(EmotionState st) {
        switch (st) {
            case ALERT: return ReasonCodes.EMOTION_ALERT;
            case SPEAKING: return ReasonCodes.EMOTION_SPEAKING;
            default: return ReasonCodes.EMOTION_CALM;
        }
    }

    private PersonaToneTag toneByChannel(PersonaChannel ch) {
        switch (ch) {
            case MORNING: return PersonaToneTag.CHEER;
            case UPCOMING:
            case REMINDER: return PersonaToneTag.ALERT;
            case SPEECH_TOOL:
            case MANUAL: return PersonaToneTag.CASUAL;
            default: return PersonaToneTag.CALM;
        }
    }

    private String buildText(PersonaContext c) {
        String p = (c.personaId != null) ? c.personaId.toLowerCase() : "default";

        if (c.channel == PersonaChannel.MORNING) {
            int cnt = (c.todayEventCount != null) ? c.todayEventCount : 0;
            if (cnt == 0) return app.getString(R.string.persona_morning_zero);

            if ("kayama".equals(p)) return morningKayama(c.emotionState, cnt);
            if ("sakamoto".equals(p)) return morningSakamoto(c.emotionState, cnt);
            if ("michelle".equals(p)) return morningMichelle(c.emotionState, cnt);

            return app.getString(R.string.persona_morning_default, cnt);
        }

        if (c.channel == PersonaChannel.UPCOMING) {
            // Upcoming title is composed by caller; here we just return summary if provided
            if (c.upcomingEventSummary != null && !c.upcomingEventSummary.trim().isEmpty()) return c.upcomingEventSummary;
            return "";
        }

        if (c.channel == PersonaChannel.REMINDER) {
            if (c.upcomingEventSummary != null && !c.upcomingEventSummary.trim().isEmpty()) return c.upcomingEventSummary;
            return app.getString(R.string.persona_reminder_fallback);
        }

        if (c.channel == PersonaChannel.DASHBOARD) {
            int cnt = (c.todayEventCount != null) ? c.todayEventCount : 0;
            String a = app.getString(R.string.persona_dashboard_count, cnt);
            String b = (c.upcomingEventSummary != null && !c.upcomingEventSummary.trim().isEmpty())
                    ? app.getString(R.string.persona_dashboard_next, c.upcomingEventSummary)
                    : app.getString(R.string.persona_dashboard_next_none);
            return a + " " + b;
        }

        // SPEECH_TOOL / MANUAL: pass-through
        return (c.upcomingEventSummary != null) ? c.upcomingEventSummary : "";
    }

    private String morningKayama(EmotionState emo, int count) {
        if (emo == EmotionState.ALERT) return app.getString(R.string.persona_kayama_alert);
        if (emo == EmotionState.SPEAKING) return app.getString(R.string.persona_kayama_speaking, count);
        return app.getString(R.string.persona_kayama_calm);
    }

    private String morningSakamoto(EmotionState emo, int count) {
        if (emo == EmotionState.ALERT) return app.getString(R.string.persona_sakamoto_alert);
        return app.getString(R.string.persona_sakamoto_default, count);
    }

    private String morningMichelle(EmotionState emo, int count) {
        if (emo == EmotionState.ALERT) return app.getString(R.string.persona_michelle_alert);
        return app.getString(R.string.persona_michelle_default, count);
    }
}