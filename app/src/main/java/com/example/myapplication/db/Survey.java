package com.example.myapplication.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity(tableName = "survey")
public class Survey {
    @ColumnInfo(name = "sid")
        @PrimaryKey (autoGenerate = true)
        public int sid;

    @ColumnInfo(name = "survey_name")
    public String surveyID;

    @ColumnInfo(name = "survey_date")
    public String surveyDate;
}
