package com.haraevanton.swapi.di;

import com.haraevanton.swapi.di.modules.ApiModule;
import com.haraevanton.swapi.di.modules.DbModule;
import com.haraevanton.swapi.di.modules.RepositoryModule;
import com.haraevanton.swapi.mvp.model.ResultRepository;
import com.haraevanton.swapi.mvp.presenters.MainActivityPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component (modules = {DbModule.class, RepositoryModule.class, ApiModule.class})
public interface AppComponent {

    void injectRepository(ResultRepository resultRepository);

    void injectMainPresenter(MainActivityPresenter mainActivityPresenter);

}
