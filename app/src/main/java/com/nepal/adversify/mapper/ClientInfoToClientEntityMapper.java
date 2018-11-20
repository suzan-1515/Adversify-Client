package com.nepal.adversify.mapper;

import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.domain.dto.Location;
import com.generic.appbase.mapper.Mapper;
import com.nepal.adversify.data.entity.ClientEntity;

import javax.inject.Inject;

public class ClientInfoToClientEntityMapper implements Mapper<ClientInfo, ClientEntity> {

    @Inject
    public ClientInfoToClientEntityMapper() {
    }

    @Override
    public ClientEntity from(ClientInfo clientModel) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.id = clientModel.id;
        clientEntity.name = clientModel.name;
        clientEntity.avatar = clientModel.avatar;
        clientEntity.lat = clientModel.location.lat;
        clientEntity.lon = clientModel.location.lon;
        return clientEntity;
    }

    @Override
    public ClientInfo to(ClientEntity clientEntity) {
        ClientInfo clientModel = new ClientInfo();
        clientModel.id = clientEntity.id;
        clientModel.name = clientEntity.name;
        clientModel.avatar = clientEntity.avatar;
        clientModel.location = new Location();
        clientModel.location.lat = clientEntity.lat;
        clientModel.location.lon = clientEntity.lon;
        return clientModel;
    }
}
