package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.Tree;

import java.util.List;

public class TreeViewModel extends AndroidViewModel {
    static AppDatabase db;
    private final LiveData<List<Tree>> treeList;

    public TreeViewModel(Application application) {
        super(application);
        db = AppDatabase.getDbInstance(application);
        treeList = db.treeDao().getAllTrees();
    }

    public LiveData<List<Tree>> getTreeList() {
        return treeList;
    }

    public List<Tree> getTreesFromSurvey(String surveyName) { return db.treeDao().getSurveyTrees(surveyName); }

    // Use a new thread to insert the tree to avoid performing database operations on the main thread
    public void insertTree(Tree tree) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.treeDao().insertTree(tree);
            }
        }).start();
    }

    public static void deleteTree(int treeID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.treeDao().deleteTree(db.treeDao().find(treeID));
            }
        }).start();
    }

    public void deleteAllTrees() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.treeDao().deleteAllTrees();
            }
        }).start();
    }
}
