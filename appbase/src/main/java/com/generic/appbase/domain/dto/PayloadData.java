package com.generic.appbase.domain.dto;

import java.io.Serializable;

public abstract class PayloadData implements Serializable {

    public static final int MERCHANT_PREVIEW_INFO = 1;
    public static final int MERCHANT_DETAIL_INFO = 2;
    public static final int CLIENT_INFO = 3;
    public static final int MERCHANT_REVIEW_INFO = 4;

    public int dataType;
    public boolean hasFile;
    public long fileId;
    public String fileName;

}
