package com.lsam.pocketsecretary.data.memo;

import android.content.Context;
import androidx.room.Room;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemoRepository {

    public interface Callback<T> {
        void onSuccess(T value);
        void onError(Exception e);
    }

    private final MemoDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MemoRepository(Context context) {
        MemoDatabase db = Room.databaseBuilder(
                context.getApplicationContext(),
                MemoDatabase.class,
                "ps_memo_db"
        ).build();
        dao = db.memoDao();
    }

    public void getAll(Callback<List<MemoEntity>> cb) {
        executor.execute(() -> {
            try { cb.onSuccess(dao.getAll()); }
            catch (Exception e) { cb.onError(e); }
        });
    }

    public void search(String query, Callback<List<MemoEntity>> cb) {
        executor.execute(() -> {
            try { cb.onSuccess(dao.search("%" + query + "%")); }
            catch (Exception e) { cb.onError(e); }
        });
    }

    public void getById(long id, Callback<MemoEntity> cb) {
        executor.execute(() -> {
            try { cb.onSuccess(dao.getById(id)); }
            catch (Exception e) { cb.onError(e); }
        });
    }

    public void create(String title, String content, Callback<Long> cb) {
        executor.execute(() -> {
            try {
                MemoEntity e = new MemoEntity();
                long now = System.currentTimeMillis();
                e.title = title;
                e.content = content;
                e.createdAt = now;
                e.updatedAt = now;
                cb.onSuccess(dao.insert(e));
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

    public void update(long id, String title, String content, Callback<Void> cb) {
        executor.execute(() -> {
            try {
                MemoEntity e = dao.getById(id);
                if (e == null) throw new IllegalStateException();
                e.title = title;
                e.content = content;
                e.updatedAt = System.currentTimeMillis();
                dao.update(e);
                cb.onSuccess(null);
            } catch (Exception ex) {
                cb.onError(ex);
            }
        });
    }

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
}
