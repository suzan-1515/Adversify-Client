/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

/*
 *  Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://mindorks.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.nepal.adversify.di.builder;


import com.generic.appbase.di.module.BaseRXModule;
import com.nepal.adversify.ui.MainActivity;
import com.nepal.adversify.ui.detail.DetailBinderModule;
import com.nepal.adversify.ui.detail.DetailFragment;
import com.nepal.adversify.ui.discover.DiscoverBinderModule;
import com.nepal.adversify.ui.discover.DiscoverFragment;
import com.nepal.adversify.ui.home.HomeBinderModule;
import com.nepal.adversify.ui.home.HomeFragment;
import com.nepal.adversify.ui.profile.ClientBinderModule;
import com.nepal.adversify.ui.profile.ClientProviderModule;
import com.nepal.adversify.ui.profile.ProfileFragment;
import com.nepal.adversify.ui.profile.ProfileSetupFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {DetailBinderModule.class, DiscoverBinderModule.class, ClientProviderModule.class, BaseRXModule.class})
    abstract DetailFragment bindDetailFragment();

    @ContributesAndroidInjector(modules = {ClientBinderModule.class, ClientProviderModule.class, BaseRXModule.class})
    abstract ProfileFragment bindProfileFragment();

    @ContributesAndroidInjector(modules = {ClientBinderModule.class, ClientProviderModule.class, BaseRXModule.class})
    abstract ProfileSetupFragment bindProfileSetupFragment();

    @ContributesAndroidInjector(modules = {DiscoverBinderModule.class, ClientProviderModule.class, BaseRXModule.class})
    abstract DiscoverFragment bindDiscoverFragment();

    @ContributesAndroidInjector(modules = {HomeBinderModule.class, ClientProviderModule.class, BaseRXModule.class})
    abstract HomeFragment bindHomeFragment();

    @ContributesAndroidInjector()
    abstract MainActivity bindMainActivity();

}
