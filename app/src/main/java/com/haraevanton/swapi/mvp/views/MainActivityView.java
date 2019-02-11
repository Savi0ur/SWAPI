package com.haraevanton.swapi.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.haraevanton.swapi.mvp.model.Result;

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

    void updateAdapterItemMoved(int position);

    void updateAdapter();

    @StateStrategyType(SkipStrategy.class)
    void showResultDetail(Result result);

    @StateStrategyType(SkipStrategy.class)
    void showSearchResults(String query, int count);

    @StateStrategyType(SkipStrategy.class)
    void showNoInternetMessage();

    @StateStrategyType(SkipStrategy.class)
    void showEmptyHistoryMessage();

    @StateStrategyType(SkipStrategy.class)
    void showSnackBarMessage(String message);

    void showProgressBar();

    void hideProgressBar();

    void hideKeyboard();

    void backPressed();

}
