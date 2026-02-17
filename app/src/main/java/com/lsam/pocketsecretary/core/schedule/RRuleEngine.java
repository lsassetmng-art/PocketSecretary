package com.lsam.pocketsecretary.core.schedule;

import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Recur;

import java.util.Date;

/**
 * RRULE engine (RFC 5545) backed by ical4j.
 */
public final class RRuleEngine {

    private RRuleEngine() {}

    public static Long computeNextOccurrenceMillis(
            long fromMillis,
            String rrule,
            String timeZoneId,
            Long untilMillis,
            Integer remainingCount
    ) {
        if (rrule == null || rrule.trim().isEmpty()) return null;

        if (remainingCount != null && remainingCount <= 0) {
            return null;
        }

        try {

            // ✔ correct constructor for your ical4j version
            Recur recur = new Recur(rrule.trim(), false);

            DateTime seed = new DateTime(new Date(fromMillis));
            DateTime after = new DateTime(new Date(fromMillis));

            Date next = recur.getNextDate(seed, after);

            if (next == null) return null;

            long nextMillis = next.getTime();

            if (untilMillis != null && nextMillis > untilMillis) {
                return null;
            }

            return nextMillis;

        } catch (Exception e) {
            return null;
        }
    }
}
