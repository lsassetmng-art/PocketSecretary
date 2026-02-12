package com.lsam.pocketsecretary.core.personaos.model;

/**
 * reason_codes: delete forbidden, extend only.
 */
public final class ReasonCodes {
    private ReasonCodes(){}

    public static final String DASHBOARD = "DASHBOARD";
    public static final String MANUAL = "MANUAL";
    public static final String MORNING_BRIEF = "MORNING_BRIEF";
    public static final String UPCOMING_EVENT = "UPCOMING_EVENT";
    public static final String REMINDER = "REMINDER";
    public static final String SPEECH_TOOL = "SPEECH_TOOL";

    public static final String EMOTION_CALM = "EMOTION_CALM";
    public static final String EMOTION_ALERT = "EMOTION_ALERT";
    public static final String EMOTION_SPEAKING = "EMOTION_SPEAKING";

    public static final String HAS_UPCOMING_SUMMARY = "HAS_UPCOMING_SUMMARY";
    public static final String HAS_TODAY_COUNT = "HAS_TODAY_COUNT";

    public static final String VERSION_ACCEPTED = "VERSION_ACCEPTED";
    public static final String VERSION_REJECTED = "VERSION_REJECTED";

    public static final String MEMORY_ACTIVE = "MEMORY_ACTIVE";
}