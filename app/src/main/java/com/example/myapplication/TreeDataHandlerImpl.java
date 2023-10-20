package com.example.myapplication;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.myapplication.db.Tree;
import java.util.HashMap;
import java.util.List;

public class TreeDataHandlerImpl implements TreeDataHandler{
    private TreeViewModel treeViewModel;
    protected HashMap<String, Tree> treeMap = new HashMap<>();

    public TreeDataHandlerImpl(TreeViewModel treeViewModel) {
        this.treeViewModel = treeViewModel;
    }

    public void insertTree(Tree newTree){ treeViewModel.insertTree(newTree);}

    @Override
    public void deleteTree(int treeID) { treeViewModel.deleteTree(treeID);}

    public LiveData<List<Tree>> getTreeList() {
        return treeViewModel.getTreeList();
    }

    @Override
    public void deleteAllTrees() {
        treeViewModel.deleteAllTrees();
    }
    public void receiveTreeData(String idNum, String latitudeNum, String longitudeNum, String diameterNum, String speciesInfo){
        Tree tree = new Tree();
        tree.idNum = idNum;
        tree.latitudeNum = latitudeNum;
        tree.longitudeNum = longitudeNum;
        tree.diameterNum = diameterNum;
        tree.speciesInfo = speciesInfo;
        if (treeMap.containsKey(idNum)){
            deleteTree(Integer.parseInt(idNum));
            treeMap.remove(idNum);
        }
        treeMap.put(idNum, tree);
        insertTree(tree);
        Log.d("treeMap", treeMap.toString());
    }
}
