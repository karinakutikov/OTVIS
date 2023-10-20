package com.example.myapplication.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tree")
public class Tree {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "id_num")
    public String idNum;
    @ColumnInfo(name = "latitude_num")
    public String latitudeNum;

    @ColumnInfo(name = "Longitude_num")
    public String longitudeNum;

    @ColumnInfo(name = "diameter_num")
    public String diameterNum;

    @ColumnInfo(name = "species_info")
    public String speciesInfo;

}
