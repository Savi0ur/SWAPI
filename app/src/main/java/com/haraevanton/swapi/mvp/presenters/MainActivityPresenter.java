package com.haraevanton.swapi.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.haraevanton.swapi.mvp.model.ResultRepository;
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.mvp.model.SwapiAnswer;
import com.haraevanton.swapi.mvp.views.MainActivityView;
import com.haraevanton.swapi.service.SwapiAPI;
import com.haraevanton.swapi.service.SwapiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    private ResultRepository resultRepository;

    private List<Result> results;
    private int pageCounter = 1;
    private int personsCounter = 0;
    private boolean isLoading;
    private boolean historyMode;
    private boolean screenMain;
    private String query;

    public MainActivityPresenter() {
        getViewState().animatePostersImg();

        resultRepository = ResultRepository.get();
        results = new ArrayList<>();
    }

    public void uploadData(int page, String characterName) {
        SwapiAPI swapiAPI = SwapiService.getSwapiApi();

        Call<SwapiAnswer> call = swapiAPI.getByName(page, characterName);

        call.enqueue(new Callback<SwapiAnswer>() {
            @Override
            public void onResponse(Call<SwapiAnswer> call, Response<SwapiAnswer> response) {
                if (response.body() != null) {
                    personsCounter = response.body().getCount();
                    if (results.isEmpty()) {
                        results.addAll(response.body().getResults());
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

    public void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItems) {

        if (!isLoading && !historyMode) {
            if ((visibleItemCount + firstVisibleItems) >= totalItemCount - 10) {
                if (results.size() < personsCounter) {
                    isLoading = true;
                    pageCounter++;
                    uploadData(pageCounter, query);
                }
            }
        }

    }

    public void addResultHistory(Result result) {
        if (!resultRepository.isHaveSameResult(result.getName())) {
            resultRepository.addResult(result);
        }
    }

    public void onButtonSearchClick(String characterName) {
        historyMode = false;
        getViewState().animateSearchBtn();
        query = characterName;
        results.clear();
        pageCounter = 1;
        getViewState().hideKeyboard();
        getViewState().showProgressBar();
        isLoading = true;
        uploadData(pageCounter, characterName);
        getViewState().hidePostersImg();
        getViewState().showList();
        getViewState().animateClearBtnToBack();
        screenMain = false;

    }

    public void onClearButtonClick() {
        historyMode = false;
        query = "";
        getViewState().clearSearchInput();
        if (screenMain) {
            getViewState().animateClearBtn();
        } else {
            getViewState().animateBackBtnToClear();
            screenMain = true;
        }
        getViewState().hideList();
        getViewState().showPostersImg();
    }

    public void onHistoryButtonClick() {
        if (!resultRepository.getResults().isEmpty()) {
            historyMode = true;
            results.clear();
            results.addAll(resultRepository.getResults());
            getViewState().onGetDataSuccess(results);
            getViewState().hidePostersImg();
            getViewState().showList();
            getViewState().clearSearchInput();
            getViewState().animateClearBtnToBack();
            screenMain = false;
        }
    }

    public void onBackPressed() {
        if (!screenMain) {
            getViewState().hideList();
            getViewState().showPostersImg();
            historyMode = false;
            screenMain = true;
            getViewState().animateBackBtnToClear();
        } else {
            getViewState().backPressed();
        }
    }

}

