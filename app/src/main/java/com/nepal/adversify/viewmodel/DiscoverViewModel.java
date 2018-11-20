package com.nepal.adversify.viewmodel;

import android.app.Application;

import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.ui.BaseViewModel;

import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public final class DiscoverViewModel extends BaseViewModel {

    private final MutableLiveData<TreeMap<String, PreviewMerchantInfo>> connectedMerchantLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> categoryTitleMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Integer> categoryIdMutableLiveData = new MutableLiveData<>();

    private final TreeMap<String, PreviewMerchantInfo> mConnectedMerchants = new TreeMap<>();

    public DiscoverViewModel(@NonNull Application application) {
        super(application);

    }

    public void addConnectedMerchants(String endpointId, PreviewMerchantInfo merchantInfo) {
        mConnectedMerchants.put(endpointId, merchantInfo);
        connectedMerchantLiveData.setValue(mConnectedMerchants);
    }

    public void removeConnectedMerchant(String endpointId) {
        if (mConnectedMerchants.containsKey(endpointId)) {
            mConnectedMerchants.remove(endpointId);
            connectedMerchantLiveData.setValue(mConnectedMerchants);
        }
    }

    public MutableLiveData<TreeMap<String, PreviewMerchantInfo>> getConnectedMerchantLiveData() {
        return connectedMerchantLiveData;
    }


    public MutableLiveData<String> getCategoryTitleMutableLiveData() {
        return categoryTitleMutableLiveData;
    }

    public void setSearchCategoryInfo(int mCategoryId, String mCategoryTitle) {
        if (categoryTitleMutableLiveData.getValue() == null) {
            categoryIdMutableLiveData.setValue(mCategoryId);
            categoryTitleMutableLiveData.setValue(mCategoryTitle);
        }
    }

    public MutableLiveData<Integer> getCategoryIdMutableLiveData() {
        return categoryIdMutableLiveData;

    }

    public void clearConnectedEndpoint() {
        mConnectedMerchants.clear();
        connectedMerchantLiveData.setValue(mConnectedMerchants);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public PreviewMerchantInfo getMerchantData(String endpointId) {
        return mConnectedMerchants.get(endpointId);
    }
}
