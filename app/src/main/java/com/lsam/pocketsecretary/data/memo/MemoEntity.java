package com.lsam.pocketsecretary.data.memo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(
        tableName = "memo",
        indices = {
                @Index("updatedAt")
        }
)
public class MemoEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String content;
    public long createdAt;
    public long updatedAt;
}
