/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.viewmodel;

import android.app.Application;

import com.nepal.adversify.data.repository.HomeRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HomeViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private final HomeRepository mHomeRepository;

    @Inject
    HomeViewModelFactory(Application application, HomeRepository mHomeRepository) {
        this.mApplication = application;
        this.mHomeRepository = mHomeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(mApplication, mHomeRepository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
