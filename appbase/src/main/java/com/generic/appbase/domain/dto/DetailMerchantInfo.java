package com.generic.appbase.domain.dto;

import java.io.Serializable;

public class DetailMerchantInfo extends PreviewMerchantInfo implements Serializable {

    public String description;
    public String website;
    public DiscountInfo discountInfo;
    public SpecialOfferInfo specialOfferInfo;
    public OpeningInfo openingInfo;
    public ReviewInfo[] reviewInfos;

}
