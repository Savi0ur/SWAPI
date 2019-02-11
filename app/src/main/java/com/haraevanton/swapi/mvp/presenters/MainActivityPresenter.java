package com.haraevanton.swapi.mvp.presenters;

import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.haraevanton.swapi.App;
import com.haraevanton.swapi.mvp.model.ResultRepository;
import com.haraevanton.swapi.mvp.model.Result;
import com.haraevanton.swapi.mvp.model.SwapiAnswer;
import com.haraevanton.swapi.mvp.views.MainActivityView;
import com.haraevanton.swapi.service.SwapiAPI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class MainActivityPresenter extends MvpPresenter<MainActivityView> {

    private static final String TAG = "MainActivityPresenter";

    @Inject
    ResultRepository resultRepository;
    @Inject
    SwapiAPI swapiAPI;

    private List<Result> results;
    private int pageCounter = 1;
    private int personsCounter = 0;
    private boolean isLoading;
    private boolean historyMode;
    private boolean screenMain;
    private String query;
    private CompositeDisposable compositeDisposable;

    public MainActivityPresenter() {
        App.getInstance().getAppComponent().injectMainPresenter(this);

        getViewState().animatePostersImg();

        screenMain = true;
        results = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        resultRepository.onCleared();
    }

    private void uploadData(int page, String characterName) {
        compositeDisposable.add(swapiAPI.getByName(page, characterName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<SwapiAnswer>() {
                    @Override
                    public void onNext(SwapiAnswer swapiAnswer) {
                        personsCounter = swapiAnswer.getCount();
                        if (personsCounter > 0) {
                            if (results.isEmpty()) {
                                historyMode = false;
                                screenMain = false;

                                results.addAll(swapiAnswer.getResults());
                                getViewState().hidePostersImg();
                                getViewState().showList();
                                getViewState().animateClearBtnToBack();
                                getViewState().onGetDataSuccess(results);
                                getViewState().showSearchResults(query, personsCounter);
                                if (personsCounter == 1) {
                                    getViewState().showResultDetail(results.get(0));
                                }
                            } else {
                                results.addAll(swapiAnswer.getResults());
                                getViewState().updateAdapter();
                            }
                        } else {
                            getViewState().showSearchResults(query, personsCounter);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        getViewState().hideProgressBar();
                        getViewState().showSnackBarMessage(e.getMessage());
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                        getViewState().hideProgressBar();
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
        resultRepository.addResult(result);
        if (historyMode){
            for (int i = 0; i < results.size(); i++){
                if (results.get(i).getName().equals(result.getName())){
                        for (int j = i; j > 0; j--) {
                            Collections.swap(results, j, j - 1);
                        }
                    getViewState().updateAdapterItemMoved(i);
                    break;
                }
            }
        }
    }

    public void onButtonSearchClick(String characterName) {
        getViewState().hideKeyboard();
        getViewState().animateSearchBtn();
        if (App.getInstance().isNetworkAvailableAndConnected()) {
            if (!isLoading) {
                getViewState().showProgressBar();
                query = characterName;
                results.clear();
                pageCounter = 1;
                isLoading = true;
                uploadData(pageCounter, characterName);
            }
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
        } else {
            getViewState().showEmptyHistoryMessage();
        }
        getViewState().hideKeyboard();
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

