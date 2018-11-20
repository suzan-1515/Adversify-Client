package com.nepal.adversify.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "client")
public class ClientEntity {

    @NonNull
    @PrimaryKey
    public String id;

    public String name;
    public String avatar;

    public double lat;
    public double lon;

}
