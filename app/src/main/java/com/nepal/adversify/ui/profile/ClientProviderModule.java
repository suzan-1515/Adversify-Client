package com.nepal.adversify.ui.profile;

import com.nepal.adversify.data.dao.ClientDAO;
import com.nepal.adversify.data.database.AppDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class ClientProviderModule {

    @Provides
    ClientDAO providesUserDAO(AppDatabase appDatabase) {
        return appDatabase.userDAO();
    }

}
