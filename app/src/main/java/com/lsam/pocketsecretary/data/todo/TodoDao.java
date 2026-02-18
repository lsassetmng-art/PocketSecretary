package com.lsam.pocketsecretary.data.todo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    long insert(TodoEntity entity);

    @Update
    void update(TodoEntity entity);

    @Delete
    void delete(TodoEntity entity);

    @Query("DELETE FROM todo WHERE id IN (:ids)")
    void deleteByIds(List<Long> ids);

    @Query("SELECT * FROM todo WHERE id = :id LIMIT 1")
    TodoEntity getById(long id);

    @Query("UPDATE todo SET status = :status WHERE id = :id")
    void updateStatus(long id, String status);

    // ---------- Filtered lists (ORDER BY fixed) ----------

    @Query("SELECT * FROM todo ORDER BY " +
            "CASE WHEN status='open' THEN 0 ELSE 1 END ASC, " +
            "CASE WHEN status='open' AND dueAt IS NULL THEN 1 ELSE 0 END ASC, " +
            "CASE WHEN status='open' THEN dueAt ELSE NULL END ASC, " +
            "priority ASC, createdAt DESC")
    List<TodoEntity> getAllOrdered();

    @Query("SELECT * FROM todo WHERE status='open' ORDER BY " +
            "CASE WHEN dueAt IS NULL THEN 1 ELSE 0 END ASC, " +
            "dueAt ASC, priority ASC, createdAt DESC")
    List<TodoEntity> getOpenOrdered();

    @Query("SELECT * FROM todo WHERE status='done' ORDER BY " +
            "priority ASC, createdAt DESC")
    List<TodoEntity> getDoneOrdered();

    // ---------- Event link (loose) ----------
    @Query("SELECT COUNT(*) FROM todo WHERE eventId = :eventId AND status='open'")
    int countOpenByEventId(long eventId);

    // ---------- Priority scope max ----------
    @Query("SELECT MAX(priority) FROM todo WHERE eventId IS NULL")
    Integer getMaxPriorityNullScope();

    @Query("SELECT MAX(priority) FROM todo WHERE eventId = :eventId")
    Integer getMaxPriorityForEvent(long eventId);

    // ---------- Scope list for move up/down (within same scope + status) ----------
    @Query("SELECT * FROM todo " +
            "WHERE ( (eventId IS NULL AND :eventId IS NULL) OR (eventId = :eventId) ) " +
            "AND status = :status " +
            "ORDER BY priority ASC, createdAt DESC")
    List<TodoEntity> getScopeByStatus(Long eventId, String status);

    // Direct priority update
    @Query("UPDATE todo SET priority = :priority WHERE id = :id")
    void updatePriority(long id, int priority);
}
