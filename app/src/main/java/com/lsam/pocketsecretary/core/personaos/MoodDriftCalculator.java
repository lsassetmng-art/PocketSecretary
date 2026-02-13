package com.lsam.pocketsecretary.core.personaos;

public class MoodDriftCalculator {

    public String applyDrift(String tone, int engagementScore) {

        if (engagementScore > 100) {
            return "friendly_open";
        }

        if (engagementScore > 30) {
            return "slightly_warm";
        }

        return tone;
    }
}
