package com.example.myapplication;

import androidx.lifecycle.LiveData;

import com.example.myapplication.db.Tree;

import java.util.List;

public interface TreeDataHandler {
    void insertTree(Tree newTree);
    void deleteTree(int treeID);
    void deleteAllTrees();
    LiveData<List<Tree>> getTreeList();
    void receiveTreeData(String idNum, String latitudeNum, String longitudeNum, String diameterNum, String speciesInfo);
}
