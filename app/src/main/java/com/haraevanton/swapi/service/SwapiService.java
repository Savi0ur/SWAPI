package com.haraevanton.swapi.service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SwapiService {

    private static final String BASE_URL = "https://swapi.co/api/";

    private static Retrofit retrofit = null;

    public static SwapiAPI getSwapiApi() {
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return  retrofit.create(SwapiAPI.class);

    }

}
