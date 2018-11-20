package com.nepal.adversify.data.prefs;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class PreferenceHelper {

    private static final String KEY_FIRST_LOAD = "key_first_load";

    private SharedPreferences mSharedPreferences;

    @Inject
    public PreferenceHelper(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    public boolean isHomeFirstLoad() {
        return mSharedPreferences.getBoolean(KEY_FIRST_LOAD, true);
    }

    public void setHomeFirstLoaded() {
        mSharedPreferences.edit().putBoolean(KEY_FIRST_LOAD, false).apply();
    }


}
