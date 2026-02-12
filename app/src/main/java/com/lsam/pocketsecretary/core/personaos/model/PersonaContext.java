package com.lsam.pocketsecretary.core.personaos.model;

public final class PersonaContext {
    public final String requestId;
    public final String appId;
    public final String personaId;
    public final String locale;
    public final String timezone;
    public final String nowIso;
    public final EmotionState emotionState;
    public final PersonaChannel channel;

    public final String upcomingEventSummary;
    public final String upcomingEventTimeIso;
    public final Integer todayEventCount;

    public PersonaContext(
            String requestId,
            String appId,
            String personaId,
            String locale,
            String timezone,
            String nowIso,
            EmotionState emotionState,
            PersonaChannel channel,
            String upcomingEventSummary,
            String upcomingEventTimeIso,
            Integer todayEventCount
    ) {
        this.requestId = requestId;
        this.appId = appId;
        this.personaId = personaId;
        this.locale = locale;
        this.timezone = timezone;
        this.nowIso = nowIso;
        this.emotionState = emotionState;
        this.channel = channel;
        this.upcomingEventSummary = upcomingEventSummary;
        this.upcomingEventTimeIso = upcomingEventTimeIso;
        this.todayEventCount = todayEventCount;
    }
}