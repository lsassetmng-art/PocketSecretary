package com.lsam.pocketsecretary.data.memo;

import androidx.room.*;

import java.util.List;
import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface MemoDao {

    @Insert
    long insert(MemoEntity entity);

    @Update
    void update(MemoEntity entity);

    @Delete
    void delete(MemoEntity entity);

    @Query("DELETE FROM memo WHERE id IN (:ids)")
    void deleteByIds(List<Long> ids);

    @Query("SELECT * FROM memo ORDER BY updatedAt DESC")
    List<MemoEntity> getAll();

    @Query("SELECT * FROM memo WHERE id = :id LIMIT 1")
    MemoEntity getById(long id);

    @Query("SELECT * FROM memo WHERE title LIKE :q OR content LIKE :q ORDER BY updatedAt DESC")
    List<MemoEntity> search(String q);

@Query("SELECT * FROM memo ORDER BY createdAt DESC")
List<MemoEntity> findAll();

}
