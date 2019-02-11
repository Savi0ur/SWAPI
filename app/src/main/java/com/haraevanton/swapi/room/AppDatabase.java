package com.haraevanton.swapi.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.haraevanton.swapi.mvp.model.Result;

@Database(entities = {Result.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "history.db";

    public abstract ResultDao resultDao();

}
