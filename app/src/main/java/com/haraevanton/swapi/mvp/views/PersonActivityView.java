package com.haraevanton.swapi.mvp.views;

import com.arellomobile.mvp.MvpView;
import com.haraevanton.swapi.room.Result;

public interface PersonActivityView extends MvpView {

    void showPersonInfo(Result result);

}
