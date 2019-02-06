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
    private int pageCounter = 1;
    private int personsCounter = 0;
    private boolean isLoading;
    private String query;

    public MainActivityPresenter() {
        getViewState().animatePostersImg();
    }

    public void uploadData(int page, String characterName) {
        SwapiAPI swapiAPI = SwapiService.getSwapiApi();

        Call<SwapiAnswer> call = swapiAPI.getByName(page, characterName);

        call.enqueue(new Callback<SwapiAnswer>() {
            @Override
            public void onResponse(Call<SwapiAnswer> call, Response<SwapiAnswer> response) {
                if (response.body() != null) {
                    personsCounter = response.body().getCount();
                    if (results == null){
                        results = response.body().getResults();
                        getViewState().onGetDataSuccess(results);
                    } else {
                        results.addAll(response.body().getResults());
                        getViewState().updateList();
                    }
                    isLoading = false;
                }

                getViewState().hideProgressBar();

            }

            @Override
            public void onFailure(Call<SwapiAnswer> call, Throwable t) {
                getViewState().hideProgressBar();
            }
        });

    }

    public void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItems){

        if (!isLoading) {
            if ((visibleItemCount+firstVisibleItems) >= totalItemCount-10){
                if (results.size() < personsCounter) {
                    isLoading = true;
                    pageCounter++;
                    uploadData(pageCounter, query);
                }
            }
        }

    }

    public void onButtonSearchClick(String characterName){
        getViewState().animateSearchBtn();
        query = characterName;
        if (results != null) {
            results.clear();
        }
        pageCounter = 1;
        getViewState().hideKeyboard();
        getViewState().showProgressBar();
        isLoading = true;
        uploadData(pageCounter, characterName);
        getViewState().hidePostersImg();
        getViewState().showList();
    }

    public void onClearButtonClick(String query) {
        if (!query.isEmpty()){
            getViewState().clearSearchInput();
            getViewState().animateClearBtn();
        }
        getViewState().hideList();
        getViewState().showPostersImg();
    }

}
