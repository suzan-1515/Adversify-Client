package com.nepal.adversify.data.repository;


import com.nepal.adversify.data.HomeLocalDataSource;
import com.nepal.adversify.domain.model.HomeModel;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import timber.log.Timber;

public class HomeRepository {

    private final HomeLocalDataSource mHomeLocalDataSource;

    @Inject
    public HomeRepository(HomeLocalDataSource homeLocalDataSource) {
        mHomeLocalDataSource = homeLocalDataSource;
        Timber.d("Initialised!");
    }

    public LiveData<HomeModel> loadHomeData() {
        Timber.d("Load home data");
        return mHomeLocalDataSource.loadHomeData();
    }

}
