package com.haraevanton.swapi;

import android.app.Application;
import android.net.ConnectivityManager;

import com.haraevanton.swapi.di.AppComponent;
import com.haraevanton.swapi.di.DaggerAppComponent;
import com.haraevanton.swapi.di.modules.ApiModule;
import com.haraevanton.swapi.di.modules.DbModule;

public class App extends Application {

    private static final String BASE_URL = "https://swapi.co/";

    public static App instance;
    private static AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        appComponent = DaggerAppComponent.builder()
                .apiModule(new ApiModule(BASE_URL))
                .dbModule(new DbModule(getApplicationContext())).build();
    }

    public static App getInstance(){
        return instance;
    }

    public AppComponent getAppComponent(){
        return appComponent;
    }

    public boolean isNetworkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;

        return isNetworkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}
