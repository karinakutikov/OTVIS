package com.example.myapplication;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.db.AppDatabase;
import com.example.myapplication.db.Tree;

import java.util.List;

public class TreeViewModel extends AndroidViewModel {
    static AppDatabase db;
    private final MutableLiveData<List<Tree>> treeList = new MutableLiveData<>();


    public TreeViewModel(Application application) {
        super(application);
        db = AppDatabase.getDbInstance(application);
        new Thread(new Runnable() {
            @Override
            public void run() { treeList.postValue(db.treeDao().getAllTrees()); }
        }).start();
    }

    public void setTreeList(List<Tree> newTreeList) { treeList.postValue(newTreeList); }
    public LiveData<List<Tree>> getTreeList() {
        return treeList;
    }

    public int findID(String surveyName) { return db.treeDao().find(surveyName); }

    // Use a new thread to insert the tree to avoid performing database operations on the main thread
    public void insertTree(Tree tree) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.treeDao().insertTree(tree);
                setTreeList(db.treeDao().getAllTrees());
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
