package com.example.translater_erbol20.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.translater_erbol20.models.DualModels;

import java.util.List;
@Dao
public interface LingvoDao {

    @Query("SELECT*FROM mmm")
    List<DualModels> getAll();

    @Insert
    void insert (DualModels model);

    @Delete
    void delete (DualModels model);

    @Update
    void update (DualModels model);

    @Query("SELECT * FROM mmm ORDER BY from_text ASC")
    List<DualModels> sortALL();


}
