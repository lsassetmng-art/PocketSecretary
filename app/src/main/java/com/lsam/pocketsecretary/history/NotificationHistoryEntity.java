package com.lsam.pocketsecretary.history;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification_history")
public class NotificationHistoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long createdAtEpochMs;
    public String source;
    public String title;
    public String text;

    public NotificationHistoryEntity(long createdAtEpochMs, String source, String title, String text) {
        this.createdAtEpochMs = createdAtEpochMs;
        this.source = source;
        this.title = title;
        this.text = text;
    }
}