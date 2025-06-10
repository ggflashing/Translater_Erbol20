package com.example.translater_erbol20.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.translater_erbol20.models.DualModels;

@Database(entities = {DualModels.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract LingvoDao lingvoDao();
}
