/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.generic.appbase.ui;

import android.app.Application;

import com.generic.appbase.utils.NetworkUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class BaseViewModel extends AndroidViewModel {

    private final MutableLiveData<Boolean> networkStatus;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        networkStatus = new MutableLiveData<>();
    }

    protected boolean isOnline() {
        //Check if connected to internet
        if (!NetworkUtils.isNetworkConnected(getApplication())) {
            getNetworkStatus().postValue(false);
            return false;
        }

        return true;
    }

    public MutableLiveData<Boolean> getNetworkStatus() {
        return networkStatus;
    }

    public static class Request {
        private final int page;
        private final int limit;

        public Request() {
            this(0, 0);
        }

        public Request(int page, int limit) {
            this.page = page;
            this.limit = limit;
        }

        public int getPage() {
            return page;
        }

        public int getLimit() {
            return limit;
        }
    }

}
