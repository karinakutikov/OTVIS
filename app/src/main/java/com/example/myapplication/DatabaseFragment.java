package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;


import com.example.myapplication.db.Survey;
import com.example.myapplication.db.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseFragment extends Fragment {
    private static final int REQUEST_WRITE_PERMISSION = 101;
    protected ConstraintLayout databaseBox;
    protected RecyclerView recyclerView;

    private TreeViewModel treeViewModel;
    private SurveyViewModel surveyViewModel;
    protected TreeListAdapter treeListAdapter;
    protected ArrayAdapter<String> dbMenuAdapter;
    private Context context;
    private GoogleMap mMap;
    private TreeDataHandlerImpl treeDataHandler;

    public DatabaseFragment() {}
    public DatabaseFragment(TreeDataHandlerImpl treeDataHandler) {
        this.treeDataHandler = treeDataHandler;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setViews(TreeViewModel treeViewModel, SurveyViewModel surveyViewModel) {
        this.treeViewModel = treeViewModel;
        this.surveyViewModel = surveyViewModel;
    }

    public SurveyViewModel getSurveyView() { return surveyViewModel; }

    public TreeViewModel getTreeView() { return treeViewModel; }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_database, container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setItemAnimator(null);
        databaseBox  = view.findViewById(R.id.database_box);
        Spinner dbMenu = view.findViewById(R.id.database_menu);
        Button clearBtn = view.findViewById(R.id.clear_cache_btn);
        Button exportBtn = view.findViewById(R.id.export_database_btn);
        initRecyclerView();

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> menuOptions = surveyViewModel.getSurveyNames();
                Collections.sort(menuOptions);
                menuOptions.add(0, "Select a session");
                dbMenuAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, menuOptions);
                dbMenuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                dbMenu.setAdapter(dbMenuAdapter);
            }
        }).start();

        //TreeViewModel viewModel = new ViewModelProvider(this).get(TreeViewModel.class);
        treeViewModel.getTreeList().observe(getViewLifecycleOwner(), new Observer<List<Tree>>() {
            @Override
            public void onChanged(List<Tree> trees) {
                treeListAdapter.setTreeList(trees, dbMenu.getSelectedItem().toString());
                Log.d("treeMapChange", trees.toString());
            }
        });

        surveyViewModel.getSurveyList().observe(getViewLifecycleOwner(), new Observer<List<Survey>>() {
            @Override
            public void onChanged(List<Survey> survey) {
                treeListAdapter.setSurveyList(survey);
                dbMenuAdapter.clear();
                List<String> newSurveyOptions = survey.stream().map(s -> s.surveyID).collect(Collectors.toList());
                Collections.sort(newSurveyOptions);
                newSurveyOptions.add(0, "Select a session");
                dbMenuAdapter.addAll(newSurveyOptions);
                dbMenu.setSelection(newSurveyOptions.size() - 1);
                Log.d("surveyListChange", survey.toString());
            }
        });


        dbMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Log.d("setOnItemSelectedListener", parent.getItemAtPosition(pos).toString());
                treeListAdapter.updateRecyclerData(parent.getItemAtPosition(pos).toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Get a ViewModelProvider and observe the LiveData.

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treeDataHandler.deleteAllTrees();
            }
        });

        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportToCSV();
            }
        });

        return view;
    }


    protected void initRecyclerView() {
        Log.e("DatabaseFragment", "initRecyclerView");
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        treeListAdapter = new TreeListAdapter(context);
        recyclerView.setAdapter(treeListAdapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                exportToCSV();
            } else {
                Toast.makeText(getActivity(), "Permission denied. Cannot export CSV.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void exportToCSV() {
//        // Fetch data from your database
        LiveData<List<Tree>> treeList = treeDataHandler.getTreeList();
//
//        // Create CSV file
        String fileName = "exported_data.csv";
        File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Log.d("treeMap", directory.toString());
        File csvFile = new File(directory, fileName);
//
        try {
            FileWriter writer = new FileWriter(csvFile);
//
//            // Write CSV header
            writer.write("id,lat,lon,diameter,species\n");
//
//            // Write data to CSV
            for (Tree tree : treeList.getValue()) {
                writer.write(tree.idNum + "," + tree.latitudeNum + "," + tree.longitudeNum + "," + tree.diameterNum + ","+ tree.speciesInfo + "\n");
            }
//
            writer.close();
//
            Toast.makeText(getActivity(), "CSV file exported to Downloads folder", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}