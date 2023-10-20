package com.example.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TreeDao {

    @Query("SELECT * FROM tree")
    LiveData<List<Tree>> getAllTrees();

    @Insert
    void insertTree(Tree... trees);

    @Query("SELECT * FROM tree WHERE id_num = :id")
    Tree find(int id);

    @Delete
    void deleteTree(Tree tree);

    @Query("DELETE FROM tree")
    void deleteAllTrees();
}
