package com.nepal.adversify.domain.callback;

public interface DiscoveryListener {

    void onDiscoveryStarted();

    void onDiscoveryFailed(Exception e);

}
