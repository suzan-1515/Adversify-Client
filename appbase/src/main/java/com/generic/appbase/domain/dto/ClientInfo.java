package com.generic.appbase.domain.dto;

import java.io.Serializable;

public class ClientInfo extends PayloadData implements Serializable {

    public String id;
    public String name;
    public String avatar;
    public Location location = new Location();

}
