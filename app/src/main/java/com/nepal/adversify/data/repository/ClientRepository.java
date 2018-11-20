package com.nepal.adversify.data.repository;

import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.utils.rx.SchedulerProvider;
import com.nepal.adversify.data.dao.ClientDAO;
import com.nepal.adversify.mapper.ClientInfoToClientEntityMapper;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import io.reactivex.Completable;
import timber.log.Timber;

public class ClientRepository {

    private final ClientDAO mClientDao;
    private final ClientInfoToClientEntityMapper cMToCEMapper;
    private final SchedulerProvider mSchedulerProvider;

    @Inject
    public ClientRepository(ClientDAO mClientDao, final ClientInfoToClientEntityMapper cMToCEMapper,
                            SchedulerProvider mSchedulerProvider) {
        this.mClientDao = mClientDao;
        this.cMToCEMapper = cMToCEMapper;
        this.mSchedulerProvider = mSchedulerProvider;
    }

    public LiveData<ClientInfo> loadClientInfo() {
        Timber.d("loadClientInfo");
        return Transformations.map(mClientDao.get(), data -> {
            if (data == null) {
                Timber.d("Empty database");
                return null;
            }
            return cMToCEMapper.to(data);
        });
    }

    public Completable saveClientInfo(ClientInfo clientModel) {
        return Completable.fromAction(() -> {
            mClientDao.insert(cMToCEMapper.from(clientModel));
        }).subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui());
    }
}
