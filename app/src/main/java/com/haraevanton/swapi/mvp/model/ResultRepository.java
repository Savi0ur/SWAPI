package com.haraevanton.swapi.mvp.model;

import com.haraevanton.swapi.App;
import com.haraevanton.swapi.room.Result;
import com.haraevanton.swapi.room.ResultDao;

import java.util.ArrayList;
import java.util.List;

public class ResultRepository {

    private static ResultRepository resultRepository;

    private ResultDao resultDao;

    private List<Result> resultsHistory;

    public static ResultRepository get(){
        if (resultRepository == null) {
            resultRepository = new ResultRepository();
        }

        return resultRepository;
    }

    private ResultRepository(){

        resultDao = App.getInstance().getDatabase().resultDao();

        if (resultsHistory == null){
            resultsHistory = new ArrayList<>();
        } else {
            resultsHistory.clear();
        }

        resultsHistory = resultDao.getAll();
    }

    public List<Result> getResults(){
        if (resultsHistory == null){
            resultsHistory = new ArrayList<>();
        } else {
            resultsHistory.clear();
        }
        resultsHistory = resultDao.getAll();
        return resultsHistory;
    }

    public void addResult(Result r){
        resultsHistory.add(r);
        resultDao.insert(r);
    }

    public boolean isHaveSameResult(String name){
        for (Result result : resultsHistory){
            if (result.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

}
