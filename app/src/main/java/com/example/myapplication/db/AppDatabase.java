package com.example.myapplication.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tree.class, Survey.class}, version  = 5)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TreeDao treeDao();
    public abstract SurveyDao surveyDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDbInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tree_database").build();
                }
            }
        }
        return INSTANCE;
    }
}