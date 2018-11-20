package com.nepal.adversify.data.dao;

import com.nepal.adversify.data.entity.ClientEntity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ClientDAO {

    @Query("SELECT * FROM client LIMIT 1")
    LiveData<ClientEntity> get();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ClientEntity clientEntity);

    @Delete
    void delete(ClientEntity clientEntity);

}
