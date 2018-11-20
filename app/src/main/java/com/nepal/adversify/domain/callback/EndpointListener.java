package com.nepal.adversify.domain.callback;

import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;

public interface EndpointListener {

    void onEndpointConnected(String endpointId, String endpointName);

    void onEndpointConnectionRejected(String endpointId, String endpointName);

    void onEndpointConnectionError(String endpointId, String endpointName);

    void onEndpointDisconnected(String endpointId);

    void onEndpointLost(String endpointId);

    void onEndpointFound(String endpointId, DiscoveredEndpointInfo discoveredEndpointInfo);
}
