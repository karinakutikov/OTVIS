package com.example.myapplication.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SurveyDao {
    @Query("SELECT * FROM survey")
    List<Survey> getAllSurveys();

    @Query("SELECT survey_name FROM survey")
    List<String> getAllSurveyNames();

    @Insert
    void insertSurvey(Survey... survey);

    @Query("SELECT * FROM survey WHERE sid = :sid")
    Survey find(int sid);
    @Query("SELECT * FROM survey WHERE survey_name = :surveyName")
    Survey find(String surveyName);

    @Delete
    void deleteSurvey(Survey survey);

    @Query("DELETE FROM survey")
    void deleteAllSurveys();

    @Query("DELETE FROM survey WHERE survey_date < :tenDaysAgo")
    void deleteOldSurveys(String tenDaysAgo);
}
