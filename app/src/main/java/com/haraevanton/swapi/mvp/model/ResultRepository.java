package com.haraevanton.swapi.mvp.model;

import android.util.Log;

import com.haraevanton.swapi.App;
import com.haraevanton.swapi.room.ResultDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResultRepository {

    private static final String TAG = "ResultRepository";

    private static ResultRepository resultRepository;

    @Inject
    ResultDao resultDao;

    private List<Result> resultsHistory;

    private CompositeDisposable compositeDisposable;

    public static ResultRepository get(){
        if (resultRepository == null) {
            resultRepository = new ResultRepository();
        }

        return resultRepository;
    }

    private ResultRepository(){

        App.getInstance().getAppComponent().injectRepository(this);

        compositeDisposable = new CompositeDisposable();

        if (resultsHistory == null){
            resultsHistory = new ArrayList<>();
        } else {
            resultsHistory.clear();
        }

        dbGetAll();
    }

    private void dbGetAll(){
        Disposable disposable = resultDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onResultsFetched, this::onError);

        compositeDisposable.add(disposable);
    }

    private void onError(Throwable throwable){
        Log.e(TAG, throwable.getMessage());
    }

    private void onResultsFetched(List<Result> results){
        resultsHistory.addAll(results);
        Collections.reverse(resultsHistory);
    }

    public List<Result> getResults(){
        return resultsHistory;
    }

    public void addResult(Result r){
        removeSameResult(r);
        resultsHistory.add(0, r);
        compositeDisposable.add(Observable.just(r)
        .observeOn(Schedulers.io())
        .subscribe(result -> resultDao.insert(r)));
    }

    private void removeSameResult(Result result){
        for (Result r : resultsHistory){
            if (r.getName().equals(result.getName())){
                resultsHistory.remove(r);
                break;
            }
        }
    }

    public void onCleared(){
        compositeDisposable.clear();
    }

}
