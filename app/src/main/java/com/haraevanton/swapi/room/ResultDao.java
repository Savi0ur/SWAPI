package com.haraevanton.swapi.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;
import io.reactivex.Single;

@Dao
public interface ResultDao {

    @Query("SELECT * FROM result")
    Single<List<Result>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Result result);

}
