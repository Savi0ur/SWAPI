package com.haraevanton.swapi.service;

import com.haraevanton.swapi.mvp.model.SwapiAnswer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SwapiAPI {

    @GET("people/")
    Call<SwapiAnswer> getByName(@Query("search") String characterName);

}
