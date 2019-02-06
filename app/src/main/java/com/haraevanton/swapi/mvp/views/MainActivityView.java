package com.haraevanton.swapi.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.haraevanton.swapi.mvp.model.Result;

import java.util.List;

public interface MainActivityView extends MvpView {

    void onGetDataSuccess(List<Result> results);

    void clearSearchInput();

    void animateClearButton();

}
