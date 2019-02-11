package com.haraevanton.swapi.di.modules;

import com.haraevanton.swapi.service.SwapiAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApiModule {

    private String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Singleton @Provides
    public SwapiAPI provideSwapiApi(Retrofit retrofit) {
        return retrofit.create(SwapiAPI.class);
    }

    @Singleton @Provides
    public Retrofit provideRetrofit(String baseUrl) {
        return new Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton @Provides
    public String provideBaseUrl(){
        return baseUrl;
    }

}
