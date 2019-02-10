package com.haraevanton.swapi.mvp.model;

import com.haraevanton.swapi.App;
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.room.ResultDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ResultRepository {

    private static ResultRepository resultRepository;

    private ResultDao resultDao;

    private List<Result> resultsHistory;

    private CompositeDisposable compositeDisposable;

    public static ResultRepository get(){
        if (resultRepository == null) {
            resultRepository = new ResultRepository();
        }

        return resultRepository;
    }

    private ResultRepository(){

        resultDao = App.getInstance().getDatabase().resultDao();

        compositeDisposable = new CompositeDisposable();

        if (resultsHistory == null){
            resultsHistory = new ArrayList<>();
        } else {
            resultsHistory.clear();
        }

        dbGetAll();
    }

    public void dbGetAll(){
        Disposable disposable = resultDao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onResultsFetched, this::onError);

        compositeDisposable.add(disposable);
    }

    public void onError(Throwable throwable){
        //TODO onError
    }

    public void onResultsFetched(List<Result> results){
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

    public void removeSameResult(Result result){
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
