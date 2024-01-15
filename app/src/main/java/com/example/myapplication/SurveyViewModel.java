package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.Survey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SurveyViewModel extends AndroidViewModel {
    static AppDatabase db;
    private final MutableLiveData<List<Survey>> surveyList = new MutableLiveData<>();

    public SurveyViewModel(Application application) {
        super(application);
        db = AppDatabase.getDbInstance(application);
        new Thread(new Runnable() {
            @Override
            public void run() { surveyList.postValue(db.surveyDao().getAllSurveys()); }
        }).start();
    }

    public void setSurveyList(List<Survey> newSurveyList) { surveyList.postValue(newSurveyList); }

    public LiveData<List<Survey>> getSurveyList() {
        return surveyList;
    }

    public List<String> getSurveyNames() { return db.surveyDao().getAllSurveyNames(); }
    public void insertSurvey(Survey survey) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.surveyDao().insertSurvey(survey);
            }
        }).start();
    }

    public static void deleteSurvey(int sid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.surveyDao().deleteSurvey(db.surveyDao().find(sid));
            }
        }).start();
    }

    public void deleteAllSurveys() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.surveyDao().deleteAllSurveys();
            }
        }).start();
    }

    public void deleteOldSurveys() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.surveyDao().deleteOldSurveys(String.valueOf(LocalDateTime.now().minusDays(10)));
            }
        }).start();
    }
}
