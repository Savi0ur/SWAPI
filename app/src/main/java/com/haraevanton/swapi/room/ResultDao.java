package com.haraevanton.swapi.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM result")
    List<Result> getAll();

    @Insert
    void insert(Result result);

    @Delete
    void delete(Result result);

}
