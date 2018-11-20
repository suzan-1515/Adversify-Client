package com.nepal.adversify.data;

import com.nepal.adversify.domain.model.HomeModel;

import androidx.lifecycle.LiveData;

public interface HomeLocalDataSource {

    LiveData<HomeModel> loadHomeData();

}
