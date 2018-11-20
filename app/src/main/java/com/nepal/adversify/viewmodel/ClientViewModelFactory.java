/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.viewmodel;

import android.app.Application;

import com.nepal.adversify.data.repository.ClientRepository;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ClientViewModelFactory implements ViewModelProvider.Factory {

    private final Application mApplication;
    private ClientRepository repository;

    @Inject
    ClientViewModelFactory(Application application, final ClientRepository repository) {
        this.mApplication = application;
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ClientViewModel.class)) {
            return (T) new ClientViewModel(mApplication, repository);
        }
        throw new IllegalArgumentException("Wrong ViewModel class");
    }
}
