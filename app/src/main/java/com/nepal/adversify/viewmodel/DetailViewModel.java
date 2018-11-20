package com.nepal.adversify.viewmodel;

import android.app.Application;

import com.generic.appbase.domain.dto.DetailMerchantInfo;
import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.ui.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

public final class DetailViewModel extends BaseViewModel {

    private final MutableLiveData<String> statusLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> connectedMerchantId = new MutableLiveData<>();
    private final MutableLiveData<PreviewMerchantInfo> merchantPreviewInfo = new MutableLiveData<>();
    private final MutableLiveData<DetailMerchantInfo> merchantDetailInfo = new MutableLiveData<>();

    public DetailViewModel(@NonNull Application application) {
        super(application);

    }

    public MutableLiveData<String> getStatusLiveData() {
        return statusLiveData;
    }

    public MutableLiveData<String> getConnectedMerchantId() {
        return connectedMerchantId;
    }

    public MutableLiveData<PreviewMerchantInfo> getMerchantPreviewInfo() {
        return merchantPreviewInfo;
    }

    public MutableLiveData<DetailMerchantInfo> getMerchantDetailInfo() {
        return merchantDetailInfo;
    }

    @Override
    protected void onCleared() {
        merchantPreviewInfo.setValue(null);
        merchantDetailInfo.setValue(null);
        statusLiveData.setValue(null);
        connectedMerchantId.setValue(null);
        super.onCleared();
    }

    public void setMerchantEndpoint(String endpointId) {
        if (connectedMerchantId.getValue() == null) {
            connectedMerchantId.setValue(endpointId);
        }
    }

    public void setPreviewInfo(PreviewMerchantInfo merchantInfo) {
        if (merchantPreviewInfo.getValue() == null) {
            merchantPreviewInfo.setValue(merchantInfo);
        }
    }

    public void updateDetailInfoImage(PreviewMerchantInfo previewMerchantInfo) {
        DetailMerchantInfo value = getMerchantDetailInfo().getValue();
        if (value != null) {
            value.previewImage = previewMerchantInfo.previewImage;
            getMerchantDetailInfo().setValue(value);
        }
    }
}
