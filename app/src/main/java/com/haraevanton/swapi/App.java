package com.haraevanton.swapi;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.net.ConnectivityManager;

import com.haraevanton.swapi.room.AppDatabase;

public class App extends Application {

    public static App instance;

    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DATABASE_NAME)
                .build();
    }

    public static App getInstance(){
        return instance;
    }

    public AppDatabase getDatabase(){
        return database;
    }

    public boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}
