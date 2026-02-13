package com.lsam.pocketsecretary.core.personaos;

import com.lsam.pocketsecretary.core.stats.UsageStatsStore;
import com.lsam.pocketsecretary.core.stats.EngagementTracker;

public class EmotionEvolutionEngine {

    private final UsageStatsStore statsStore;
    private final EngagementTracker engagementTracker;
    private final int maxAlertPerHour;

    public EmotionEvolutionEngine(
            UsageStatsStore stats,
            EngagementTracker tracker,
            int maxPerHour) {

        this.statsStore = stats;
        this.engagementTracker = tracker;
        this.maxAlertPerHour = maxPerHour;
    }

    public ToneAdjustment evolve(
            String baseTone,
            String reasonCode) {

        // 1. Reason mapping
        ReasonToneMapper mapper = new ReasonToneMapper();
        String tone = mapper.mapReasonToTone(reasonCode, baseTone);

        // 2. Alert frequency control
        int alertCount = statsStore.getAlertCountThisHour();
        if (alertCount >= maxAlertPerHour) {
            tone = "calm_softened";
        }

        // 3. Mood drift by engagement
        MoodDriftCalculator drift = new MoodDriftCalculator();
        tone = drift.applyDrift(tone, engagementTracker.getScore());

        return new ToneAdjustment(tone);
    }
}
