/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.ui.discover;

import com.generic.appbase.domain.dto.PreviewMerchantInfo;
import com.generic.appbase.domain.event.OnItemClickCallback;
import com.nepal.adversify.domain.callback.ConnectionListener;
import com.nepal.adversify.domain.callback.DiscoveryListener;
import com.nepal.adversify.domain.callback.EndpointListener;
import com.nepal.adversify.domain.callback.PayloadListener;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class DiscoverBinderModule {

    @Binds
    abstract OnItemClickCallback<PreviewMerchantInfo> bindsOnItemClickCallback(DiscoverFragment discoverFragment);

    @Binds
    abstract EndpointListener bindsEndpointCallback(DiscoverFragment discoverFragment);

    @Binds
    abstract PayloadListener bindsPayloadCallback(DiscoverFragment discoverFragment);

    @Binds
    abstract ConnectionListener bindsConnectionCallback(DiscoverFragment discoverFragment);

    @Binds
    abstract DiscoveryListener bindsDiscoveryCallback(DiscoverFragment discoverFragment);

}
