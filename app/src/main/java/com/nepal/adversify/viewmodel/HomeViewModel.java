package com.nepal.adversify.viewmodel;

import android.app.Application;

import com.generic.appbase.ui.BaseViewModel;
import com.nepal.adversify.data.repository.HomeRepository;
import com.nepal.adversify.domain.model.HomeModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

public final class HomeViewModel extends BaseViewModel {

    final private MutableLiveData<Request> request = new MutableLiveData<>();
    private HomeRepository mHomeRepository;
    private LiveData<HomeModel> homeModelMutableLiveData = Transformations.switchMap(request,
            input -> mHomeRepository.loadHomeData()
    );

    public HomeViewModel(@NonNull Application application,
                         HomeRepository mHomeRepository) {
        super(application);
        this.mHomeRepository = mHomeRepository;

    }

    public void loadHomeMenus() {
        if (homeModelMutableLiveData.getValue() == null)
            request.setValue(new Request());
    }

    public LiveData<HomeModel> getHomeModelLiveData() {
        return homeModelMutableLiveData;
    }

}
