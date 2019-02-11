package com.haraevanton.swapi.di.modules;

import com.haraevanton.swapi.mvp.model.ResultRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton @Provides
    public ResultRepository provideRepository() {
        return ResultRepository.get();
    }
}
