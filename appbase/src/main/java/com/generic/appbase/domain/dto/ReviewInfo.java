package com.generic.appbase.domain.dto;

import java.io.Serializable;

public class ReviewInfo extends PayloadData implements Serializable {

    public String clientId;
    public String clientName;
    public int star;
    public String content;

}
