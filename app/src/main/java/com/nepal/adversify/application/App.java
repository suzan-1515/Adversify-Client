/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.application;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.nepal.adversify.BuildConfig;
import com.nepal.adversify.di.component.DaggerAppComponent;
import com.nepal.adversify.service.ConnectionService;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import timber.log.Timber;

public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> activityInjector;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    ConnectionsClient mConnectionsClient;

    @Override
    public void onCreate() {
        super.onCreate();

        DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        mCalligraphyConfig))
                .build());

        ConnectionService.getInstance().setGoogleApiClient(mConnectionsClient);

    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityInjector;
    }

    @Override
    public void onTerminate() {
        ConnectionService.getInstance().disconnect();
        super.onTerminate();

    }

}
