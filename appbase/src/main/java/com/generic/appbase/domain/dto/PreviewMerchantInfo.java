package com.generic.appbase.domain.dto;

import java.io.Serializable;

public class PreviewMerchantInfo extends PayloadData implements Serializable {

    public String title;
    public String address;
    public String contact;
    public String specialOffer;
    public String discount;
    public String previewImage;
    public int rating;
    public Location location;
    public String distance;

}
