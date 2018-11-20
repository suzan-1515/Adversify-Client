/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.nepal.adversify.ui.profile;

import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.mapper.Mapper;
import com.nepal.adversify.data.entity.ClientEntity;
import com.nepal.adversify.mapper.ClientInfoToClientEntityMapper;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ClientBinderModule {

    @Binds
    abstract Mapper<ClientInfo, ClientEntity> providesClientModelToUserEntityMapper(ClientInfoToClientEntityMapper mapper);

}
