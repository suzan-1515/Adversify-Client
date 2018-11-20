package com.nepal.adversify.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.generic.appbase.ui.BaseActivity;
import com.nepal.adversify.R;

import androidx.navigation.Navigation;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import timber.log.Timber;

public class MainActivity extends BaseActivity {


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        Timber.d("onViewReady");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

}
