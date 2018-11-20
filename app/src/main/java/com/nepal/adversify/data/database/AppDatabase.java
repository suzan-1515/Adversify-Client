package com.nepal.adversify.data.database;

import com.nepal.adversify.BuildConfig;
import com.nepal.adversify.data.dao.ClientDAO;
import com.nepal.adversify.data.entity.ClientEntity;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ClientEntity.class}, version = BuildConfig.VERSION_CODE)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClientDAO userDAO();

}
