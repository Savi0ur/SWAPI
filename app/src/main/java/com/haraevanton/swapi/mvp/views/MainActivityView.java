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

    void showSearchResults(String query, int count);

    void showNoInternetMessage();

    void showSnackBarMessage(String message);

    void showProgressBar();

    void hideProgressBar();

    void hideKeyboard();

    void backPressed();

}
