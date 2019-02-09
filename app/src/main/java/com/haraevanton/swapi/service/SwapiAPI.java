package com.haraevanton.swapi.service;

import com.haraevanton.swapi.mvp.model.SwapiAnswer;

import io.reactivex.Observable;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SwapiAPI {

    @GET("people/")
    Observable<SwapiAnswer> getByName(@Query("page") int page, @Query("search") String characterName);

}
