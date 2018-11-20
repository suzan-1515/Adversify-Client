package com.nepal.adversify.service;

import com.generic.appbase.domain.dto.ClientInfo;
import com.generic.appbase.domain.dto.PayloadData;
import com.generic.appbase.utils.SerializationUtils;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.nepal.adversify.domain.callback.ConnectionListener;
import com.nepal.adversify.domain.callback.DiscoveryListener;
import com.nepal.adversify.domain.callback.EndpointListener;
import com.nepal.adversify.domain.callback.PayloadListener;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.collection.SimpleArrayMap;
import timber.log.Timber;

import static com.generic.appbase.connection.ConnectionInfo.STRATEGY;

public class ConnectionService {

    private static final ConnectionService ourInstance = new ConnectionService();
    private ConnectionsClient mConnectionsClient;

    private DiscoveryListener discoveryListener;
    private EndpointListener endpointListener;
    private PayloadListener payloadListener;
    private ConnectionListener connectionListener;

    private PayloadHandler payloadHandler = new PayloadHandler();

    private boolean isDiscoveryStarted = false;

    private EndpointDiscoveryCallback endpointDiscoveryCallback = new EndpointDiscoveryCallback() {
        @Override
        public void onEndpointFound(@NonNull String s, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
            getEndpointListener().onEndpointFound(s, discoveredEndpointInfo);
        }

        @Override
        public void onEndpointLost(@NonNull String s) {
            getEndpointListener().onEndpointLost(s);
        }
    };
    private ConnectionLifecycleCallback connectionLifecycleCallback = new ConnectionLifecycleCallback() {

        private final SimpleArrayMap<String, ConnectionInfo> incomingConnection = new SimpleArrayMap<>();

        @Override
        public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
            incomingConnection.put(endpointId, connectionInfo);
            mConnectionsClient.acceptConnection(endpointId, payloadHandler);
        }

        @Override
        public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
            ConnectionInfo connectionInfo = incomingConnection.remove(endpointId);
            if (result.getStatus().isSuccess()) {
                switch (result.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK:
                        // We're connected! Can now start sending and receiving data.
                        getEndpointListener().onEndpointConnected(endpointId, connectionInfo.getEndpointName());
                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        // The connection was rejected by one or both sides.
                        getEndpointListener().onEndpointConnectionRejected(endpointId, connectionInfo.getEndpointName());
                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        // The connection broke before it was able to be accepted.
                        getEndpointListener().onEndpointConnectionError(endpointId, connectionInfo.getEndpointName());
                        break;
                }
            }
        }

        @Override
        public void onDisconnected(@NonNull String endpointId) {
            getEndpointListener().onEndpointDisconnected(endpointId);
        }
    };

    private ConnectionService() {
    }

    public static ConnectionService getInstance() {
        return ourInstance;
    }

    public void startDiscovery() {
        if (!isDiscoveryStarted) {
            mConnectionsClient.startDiscovery(
                    com.generic.appbase.connection.ConnectionInfo.NEARBY_CONNECTION_SERVICE_ID,
                    endpointDiscoveryCallback,
                    new DiscoveryOptions.Builder()
                            .setStrategy(STRATEGY)
                            .build()
            )
                    .addOnSuccessListener(
                            unusedResult -> {
                                isDiscoveryStarted = true;
                                getDiscoveryListener().onDiscoveryStarted();
                            })
                    .addOnFailureListener(
                            e -> {
                                Timber.e(e);
                                getDiscoveryListener().onDiscoveryFailed(e);
                            });
        } else {
            Timber.d("Discovery already started");
        }
    }

    public void setGoogleApiClient(ConnectionsClient mConnectionsClient) {
        this.mConnectionsClient = mConnectionsClient;
    }

    public ConnectionsClient getConnectionsClient() {
        return mConnectionsClient;
    }

    public void requestConnection(String endpointId, ClientInfo clientInfo, int categoryId) {
        mConnectionsClient.requestConnection(
                String.format(Locale.getDefault(), "%d:%s", categoryId, clientInfo.name),
                endpointId,
                connectionLifecycleCallback
        ).addOnSuccessListener(
                unusedResult -> {
                    getConnectionListener().onConnectionSuccessful(endpointId, clientInfo);
                })
                .addOnFailureListener(
                        e -> {
                            getConnectionListener().onConnectionFailed(e, endpointId, clientInfo);
                        });
    }

    public void disconnect() {
        mConnectionsClient.stopAllEndpoints();
        stopDiscovery();
    }

    public void stopDiscovery() {
        mConnectionsClient.stopDiscovery();
        isDiscoveryStarted = false;
    }

    public DiscoveryListener getDiscoveryListener() {
        return discoveryListener;
    }

    public void setDiscoveryListener(DiscoveryListener discoveryListener) {
        this.discoveryListener = discoveryListener;
    }

    public EndpointListener getEndpointListener() {
        return endpointListener;
    }

    public void setEndpointListener(EndpointListener endpointListener) {
        this.endpointListener = endpointListener;
    }

    public PayloadListener getPayloadListener() {
        return payloadListener;
    }

    public void setPayloadListener(PayloadListener payloadListener) {
        this.payloadListener = payloadListener;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    public void setConnectionListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public PayloadHandler getPayloadHandler() {
        return payloadHandler;
    }

    public class PayloadHandler extends PayloadCallback {
        private final SimpleArrayMap<Long, Payload> incomingPayloads = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, Payload> outgoingPayloads = new SimpleArrayMap<>();
        private final SimpleArrayMap<Long, PayloadData> filePayload = new SimpleArrayMap<>();

        @Override
        public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
            if (payload.getType() == Payload.Type.BYTES) {
                PayloadData payloadData = (PayloadData) SerializationUtils.deSerializeFromByteArray(payload.asBytes());
                if (payloadData.hasFile) {
                    filePayload.put(payloadData.fileId, payloadData);
                }
                getPayloadListener().onBytePayloadReceived(endpointId, payload.getId(), payloadData);
            } else
                incomingPayloads.put(payload.getId(), payload);
        }

        @Override
        public void onPayloadTransferUpdate(@NonNull String endpointId, @NonNull PayloadTransferUpdate update) {
            long payloadId = update.getPayloadId();
            if (incomingPayloads.containsKey(payloadId)) {
                Payload payload = incomingPayloads.get(payloadId);
                switch (payload.getType()) {
                    case Payload.Type.STREAM:
                        break;
                    case Payload.Type.FILE:
                        Timber.d("File payload update..");
                        if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                            incomingPayloads.remove(payloadId);
                            PayloadData filePayloadData = filePayload.remove(payload.getId());
                            File payloadFile = payload.asFile().asJavaFile();
                            File renamedFile = new File(payloadFile.getParentFile(), filePayloadData.fileName);
                            payloadFile.renameTo(renamedFile);
                            filePayloadData.fileName = renamedFile.getAbsolutePath();
                            getPayloadListener().onFilePayloadReceived(endpointId, payload.getId(), filePayloadData);
                        }
                        break;
                }
            } else if (outgoingPayloads.containsKey(payloadId)) {
                if (update.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
                    outgoingPayloads.remove(payloadId);
                    getPayloadListener().onPayloadSent(payloadId, endpointId);
                }
            }
        }

        public void sendPayload(String endpointID, Payload payload) {
            outgoingPayloads.put(payload.getId(), payload);
            mConnectionsClient.sendPayload(endpointID, payload);
        }
    }

}
