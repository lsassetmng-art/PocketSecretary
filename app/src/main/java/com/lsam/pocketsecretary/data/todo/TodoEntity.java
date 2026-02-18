package com.lsam.pocketsecretary.data.todo;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "todo",
        indices = {
                @Index("status"),
                @Index("eventId"),
                @Index("dueAt")
        }
)
public class TodoEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;     // not null by operation
    public String content;   // nullable
    public long createdAt;
    public Long dueAt;       // nullable (epoch millis)
    public String status;    // "open" | "done"
    public int priority;     // scope-stable
    public Long eventId;     // nullable (loose link)
}
