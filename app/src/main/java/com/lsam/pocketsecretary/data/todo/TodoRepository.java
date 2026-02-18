package com.lsam.pocketsecretary.data.todo;

import android.content.Context;

import androidx.room.Room;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoRepository {

    public interface Callback<T> {
        void onSuccess(T value);
        void onError(Exception e);
    }

    public enum FilterType {
        ALL, OPEN, DONE
    }

    private final TodoDatabase db;
    private final TodoDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public TodoRepository(Context context) {
        db = Room.databaseBuilder(
                context.getApplicationContext(),
                TodoDatabase.class,
                "ps_todo_db"
        ).build();
        dao = db.todoDao();
    }

    // ------------------------------
    // Read
    // ------------------------------
    public void getTodos(FilterType filter, Callback<List<TodoEntity>> cb) {
        executor.execute(() -> {
            try {
                List<TodoEntity> res;
                if (filter == FilterType.OPEN) res = dao.getOpenOrdered();
                else if (filter == FilterType.DONE) res = dao.getDoneOrdered();
                else res = dao.getAllOrdered();
                cb.onSuccess(res);
            } catch (Exception e) {
                cb.onError(e);
            }
        });
    }

    public void getById(long id, Callback<TodoEntity> cb) {
        executor.execute(() -> {
            try {
                cb.onSuccess(dao.getById(id));
            } catch (Exception e) {
                cb.onError(e);
            }
        });
    }

    // ------------------------------
    // Create
    // ------------------------------
    public void create(String title, String content, Long dueAt, String eventId, Callback<Long> cb) {
        executor.execute(() -> {
            try {
                TodoEntity e = new TodoEntity();
                e.title = title;
                e.content = content;
                e.createdAt = System.currentTimeMillis();
                e.dueAt = dueAt;
                e.status = "open";
                e.eventId = eventId;
                e.priority = nextPriority(eventId);
                long id = dao.insert(e);
                cb.onSuccess(id);
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Update
    // ------------------------------
    public void update(long id, String title, String content, Long dueAt, String eventId, Callback<Void> cb) {
        executor.execute(() -> {
            try {
                TodoEntity e = dao.getById(id);
                if (e == null) throw new IllegalStateException("Todo not found");

                boolean scopeChanged = !sameString(e.eventId, eventId);

                e.title = title;
                e.content = content;
                e.dueAt = dueAt;

                if (scopeChanged) {
                    String newEventId = eventId;
                    db.runInTransaction(() -> {
                        e.eventId = newEventId;
                        e.priority = nextPriorityInternal(newEventId);
                        dao.update(e);
                    });
                } else {
                    e.eventId = eventId;
                    dao.update(e);
                }

                cb.onSuccess(null);
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Toggle
    // ------------------------------
    public void toggleStatus(long id, Callback<Void> cb) {
        executor.execute(() -> {
            try {
                TodoEntity e = dao.getById(id);
                if (e == null) throw new IllegalStateException("Todo not found");

                String next = "open".equals(e.status) ? "done" : "open";
                dao.updateStatus(id, next);
                cb.onSuccess(null);
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Delete
    // ------------------------------
    public void deleteByIds(List<Long> ids, Callback<Void> cb) {
        executor.execute(() -> {
            try {
                dao.deleteByIds(ids);
                cb.onSuccess(null);
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Move
    // ------------------------------
    public void moveUp(long id, Callback<Void> cb) {
        moveInternal(id, true, cb);
    }

    public void moveDown(long id, Callback<Void> cb) {
        moveInternal(id, false, cb);
    }

    private void moveInternal(long id, boolean up, Callback<Void> cb) {
        executor.execute(() -> {
            try {
                TodoEntity target = dao.getById(id);
                if (target == null) throw new IllegalStateException("Todo not found");

                List<TodoEntity> scope = dao.getScopeByStatus(target.eventId, target.status);

                int idx = indexOf(scope, id);
                if (idx < 0) throw new IllegalStateException("Scope mismatch");

                int otherIdx = up ? idx - 1 : idx + 1;
                if (otherIdx < 0 || otherIdx >= scope.size()) {
                    cb.onSuccess(null);
                    return;
                }

                TodoEntity other = scope.get(otherIdx);

                int pA = target.priority;
                int pB = other.priority;

                db.runInTransaction(() -> {
                    dao.updatePriority(target.id, pB);
                    dao.updatePriority(other.id, pA);
                });

                cb.onSuccess(null);

            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Count by Event
    // ------------------------------
    public void countOpenByEventId(String eventId, Callback<Integer> cb) {
        executor.execute(() -> {
            try {
                cb.onSuccess(dao.countOpenByEventId(eventId));
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    // ------------------------------
    // Priority
    // ------------------------------
    private int nextPriority(String eventId) {
        Integer max = (eventId == null)
                ? dao.getMaxPriorityNullScope()
                : dao.getMaxPriorityForEvent(eventId);

        return (max == null) ? 10 : (max + 10);
    }

    private int nextPriorityInternal(String eventId) {
        Integer max = (eventId == null)
                ? dao.getMaxPriorityNullScope()
                : dao.getMaxPriorityForEvent(eventId);

        return (max == null) ? 10 : (max + 10);
    }

    private static boolean sameString(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    private static int indexOf(List<TodoEntity> list, long id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) return i;
        }
        return -1;
    }
}
