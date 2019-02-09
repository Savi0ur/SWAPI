package com.haraevanton.swapi.mvp.presenters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.haraevanton.swapi.App;
import com.haraevanton.swapi.mvp.model.ResultRepository;
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.mvp.model.SwapiAnswer;
import com.haraevanton.swapi.mvp.views.MainActivityView;
import com.haraevanton.swapi.service.SwapiAPI;
import com.haraevanton.swapi.service.SwapiService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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
    private CompositeDisposable compositeDisposable;

    public MainActivityPresenter() {
        getViewState().animatePostersImg();

        screenMain = true;
        resultRepository = ResultRepository.get();
        results = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        resultRepository.onCleared();
    }

    public void uploadData(int page, String characterName) {
        SwapiAPI swapiAPI = SwapiService.getSwapiApi();

        compositeDisposable.add(swapiAPI.getByName(page, characterName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SwapiAnswer>() {
                    @Override
                    public void onNext(SwapiAnswer swapiAnswer) {
                        if (swapiAnswer != null) {
                            personsCounter = swapiAnswer.getCount();
                            if (personsCounter > 0) {
                                if (results.isEmpty()) {
                                    historyMode = false;
                                    screenMain = false;

                                    results.addAll(swapiAnswer.getResults());

                                    getViewState().showProgressBar();
                                    getViewState().hidePostersImg();
                                    getViewState().showList();
                                    getViewState().animateClearBtnToBack();
                                    getViewState().onGetDataSuccess(results);
                                    getViewState().showSearchResults(query, personsCounter);
                                } else {
                                    results.addAll(swapiAnswer.getResults());
                                    getViewState().updateList();
                                }
                            } else {
                                getViewState().showSearchResults(query, personsCounter);
                            }
                            isLoading = false;
                        }
                        getViewState().hideProgressBar();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getViewState().hideProgressBar();
                        getViewState().showSnackBarMessage("Server side error");
                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    public void onRecyclerViewScrolled(int visibleItemCount, int totalItemCount, int firstVisibleItems) {
        if (!isLoading && !historyMode) {
            if ((visibleItemCount + firstVisibleItems) >= totalItemCount - 10) {
                if (results.size() < personsCounter) {
                    isLoading = true;
                    pageCounter++;
                    getViewState().showProgressBar();
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
        getViewState().hideKeyboard();
        getViewState().animateSearchBtn();
        if (App.getInstance().isNetworkAvailableAndConnected()) {
            query = characterName;
            results.clear();
            pageCounter = 1;
            isLoading = true;
            uploadData(pageCounter, characterName);
        } else {
            getViewState().showNoInternetMessage();
        }

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

