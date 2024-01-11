package com.example.myapplication.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;

@Entity(tableName = "tree",
        foreignKeys = { @ForeignKey(
                        entity = Survey.class, parentColumns = "sid",
                        childColumns = "survey_id", onDelete = ForeignKey.CASCADE)},
        indices = {@Index("survey_id")})
public class Tree {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "survey_id")
    public int sid;

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
