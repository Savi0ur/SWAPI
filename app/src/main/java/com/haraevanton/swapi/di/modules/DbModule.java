package com.haraevanton.swapi.di.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.haraevanton.swapi.room.AppDatabase;
import com.haraevanton.swapi.room.ResultDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    private Context context;

    public DbModule(Context context) {
        this.context = context;
    }

    @Singleton @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton @Provides
    public AppDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .build();
    }

    @Singleton @Provides
    public ResultDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.resultDao();
    }

}
