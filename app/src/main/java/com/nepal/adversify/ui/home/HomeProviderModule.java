package com.nepal.adversify.ui.home;

import android.content.Context;

import com.nepal.adversify.data.HomeLocalDataSource;
import com.nepal.adversify.data.HomeLocalDataSourceImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HomeProviderModule {

    @Provides
    @Singleton
    HomeLocalDataSource proHomeLocalDataSource(Context context) {
        return new HomeLocalDataSourceImpl(context);
    }

}
