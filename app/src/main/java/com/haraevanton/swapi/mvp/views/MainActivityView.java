package com.haraevanton.swapi.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.haraevanton.swapi.room.Result;

import java.util.List;

public interface MainActivityView extends MvpView {

    void onGetDataSuccess(List<Result> results);

    void clearSearchInput();

    void animateClearBtn();

    void animateBackBtnToClear();

    void animateClearBtnToBack();

    void animateSearchBtn();

    void animatePostersImg();

    void showPostersImg();

    void hidePostersImg();

    void showList();

    void hideList();

    void updateList();

    void showProgressBar();

    void hideProgressBar();

    void hideKeyboard();

}
