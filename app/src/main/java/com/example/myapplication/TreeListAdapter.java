package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Survey;
import com.example.myapplication.db.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.MyViewHolder> {

    private Context context;
    private List<Survey> surveyList = new ArrayList<>();

    private List<Tree> treeList = new ArrayList<>();
    public int currentSurvey;
    private List<Tree> treeListBySurvey = new ArrayList<>();
    public TreeListAdapter(Context context) {
        this.context = context;
    }

    public List<Tree> filterList(int surveyID) {
        return treeList.stream().filter(t -> surveyID == (t.sid)).collect(Collectors.toList());
    }

    public int findID(String surveyName) {
        for(Survey s : surveyList) {
            if(s.surveyID.equals(surveyName)) return s.sid;
        }
        return -1;
    }

    public void updateRecyclerData(String surveyName) {
        this.treeListBySurvey = filterList(findID(surveyName));
        notifyDataSetChanged(); //This will update recyclerview
    }

    public void setTreeList(List<Tree> treeList, String currentSurvey) {
        this.treeList = treeList;
        updateRecyclerData(currentSurvey);
    }

    public void setSurveyList(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }
    @NonNull
    @Override
    public TreeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeListAdapter.MyViewHolder holder, int position) {
        holder.tvLatitude.setText(this.treeListBySurvey.get(position).latitudeNum);
        holder.tvLongitude.setText(this.treeListBySurvey.get(position).longitudeNum);
        holder.tvIdNum.setText(this.treeListBySurvey.get(position).idNum);
        holder.tvDiameterNum.setText(this.treeListBySurvey.get(position).diameterNum);
        holder.tvSpeciesInfo.setText(this.treeListBySurvey.get(position).speciesInfo);
    }

    @Override
    public int getItemCount() {
        return this.treeListBySurvey.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvIdNum;
        TextView tvLatitude;
        TextView tvLongitude;
        TextView tvDiameterNum;
        TextView tvSpeciesInfo;
        Button goBtn;
        Button delBtn;

        public MyViewHolder(View view) {
            super(view);
            Log.e("TreeListAdapter", "MyViewHolder");
            tvIdNum = view.findViewById(R.id.tvIdNum);
            tvLatitude = view.findViewById(R.id.tvLatitude);
            tvLongitude = view.findViewById(R.id.tvLongitude);
            tvDiameterNum = view.findViewById(R.id.tvDiameterNum);
            tvSpeciesInfo = view.findViewById(R.id.tvSpeciesInfo);
            delBtn = view.findViewById(R.id.tvDeleteTree);
            goBtn = view.findViewById(R.id.tvLocateTree);
            delBtn.setOnClickListener(v -> {
                Log.e("TreeListAdapter", "delete Btn");
                TreeViewModel.deleteTree(Integer.parseInt(tvIdNum.getText().toString()));
            });
            goBtn.setOnClickListener(v -> {
                Log.e("TreeListAdapter", "go Btn");
                MainActivity.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.3, -75.8), 20));
            });
        }
    }
}
