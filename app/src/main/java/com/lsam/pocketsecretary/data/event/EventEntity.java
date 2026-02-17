package com.lsam.pocketsecretary.data.event;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Local event entity designed for future Google Calendar mapping.
 */
@Entity(
        tableName = "events",
        indices = {
                @Index(value = {"startAt"}),
                @Index(value = {"localOnly"}),
                @Index(value = {"googleEventId"})
        }
)
public class EventEntity {

    @PrimaryKey
    @NonNull
    public String id;

    public String title;
    public String description;
    public String location;

    public long startAt;
    public long endAt;

    public boolean allDay;

    public String timeZone;

    /**
     * RRULE body only (e.g. "FREQ=WEEKLY;BYDAY=MO,WE,FR").
     */
    public String recurrenceRule;

    public Long recurrenceUntil;

    public Integer recurrenceCount;

    /**
     * NEW:
     * Tracks the last occurrence start time that has been processed.
     * Prevents recurrence drift and duplication.
     */
    public Long lastOccurrenceAt;

    public int reminderBeforeMin;

    public boolean localOnly;

    public String googleEventId;
}
