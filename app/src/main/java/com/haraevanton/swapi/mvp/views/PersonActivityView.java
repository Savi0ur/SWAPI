package com.haraevanton.swapi.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.haraevanton.swapi.mvp.model.Result;

public interface PersonActivityView extends MvpView {

    void showPersonInfo(Result result);

}
