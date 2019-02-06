package com.haraevanton.swapi.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.haraevanton.swapi.mvp.model.Result;
import com.haraevanton.swapi.mvp.model.SwapiAnswer;
import com.haraevanton.swapi.mvp.views.MainActivityView;
import com.haraevanton.swapi.service.SwapiAPI;
import com.haraevanton.swapi.service.SwapiService;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    private List<Result> results;

    public MainActivityPresenter() {
        uploadData("");
    }

    public void uploadData(String characterName) {
        SwapiAPI swapiAPI = SwapiService.getSwapiApi();

        Call<SwapiAnswer> call = swapiAPI.getByName(characterName);

        call.enqueue(new Callback<SwapiAnswer>() {
            @Override
            public void onResponse(Call<SwapiAnswer> call, Response<SwapiAnswer> response) {
                if (response.body() != null) {
                    results = response.body().getResults();
                    getViewState().onGetDataSuccess(results);
                }

            }

            @Override
            public void onFailure(Call<SwapiAnswer> call, Throwable t) {

            }
        });

    }

    public void onClearButtonClick(String query) {
        if (!query.isEmpty()){
            getViewState().clearSearchInput();
            getViewState().animateClearButton();
        }

    }

}
