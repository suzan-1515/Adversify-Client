package com.nepal.adversify.domain.callback;

import com.generic.appbase.domain.dto.ClientInfo;

public interface ConnectionListener {
    void onConnectionSuccessful(String endpointId, ClientInfo clientInfo);

    void onConnectionFailed(Exception e, String endpointId, ClientInfo clientInfo);
}
