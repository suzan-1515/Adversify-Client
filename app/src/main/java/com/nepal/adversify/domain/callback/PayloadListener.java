package com.nepal.adversify.domain.callback;

import com.generic.appbase.domain.dto.PayloadData;

public interface PayloadListener {

    void onBytePayloadReceived(String endpointId, long id, Object object);

    void onPayloadSent(long payloadId, String endpointId);

    void onFilePayloadReceived(String endpointId, long id, PayloadData filePayloadData);
}
