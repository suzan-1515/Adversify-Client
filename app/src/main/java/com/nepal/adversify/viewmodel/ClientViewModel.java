package com.nepal.adversify.viewmodel;

import android.app.Application;
import android.net.Uri;
import android.text.TextUtils;

import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.domain.dto.PayloadData;
import com.generic.appbase.ui.BaseViewModel;
import com.generic.appbase.utils.FileUtils;
import com.nepal.adversify.data.repository.ClientRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import io.reactivex.Completable;

public final class ClientViewModel extends BaseViewModel {

    final private MutableLiveData<Request> request = new MutableLiveData<>();
    private ClientRepository mClientRepository;
    private final LiveData<ClientInfo> clientLiveData = Transformations.switchMap(request,
            input -> mClientRepository.loadClientInfo()
    );

    public ClientViewModel(@NonNull Application application, ClientRepository mClientRepository) {
        super(application);
        this.mClientRepository = mClientRepository;
    }

    public Completable saveClientInfo(ClientInfo clientModel) {
        return mClientRepository.saveClientInfo(clientModel);
    }

    public LiveData<ClientInfo> getClientLiveData() {
        return clientLiveData;
    }

    public ClientInfo getClientInfo() {
        ClientInfo clientInfo = clientLiveData.getValue();
        if (!TextUtils.isEmpty(clientInfo.avatar)) {
            clientInfo.fileName = FileUtils.getExtensionWithName(getApplication(), Uri.parse(clientInfo.avatar));
            clientInfo.hasFile = true;
        } else {
            clientInfo.hasFile = false;
        }

        clientInfo.dataType = PayloadData.CLIENT_INFO;

        return clientInfo;
    }

    public void loadClientData() {
        if (clientLiveData.getValue() == null)
            request.setValue(new Request());
    }
}
