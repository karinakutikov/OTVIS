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

import com.example.myapplication.db.Tree;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.MyViewHolder> {

    private Context context;
    private List<Tree> treeList = new ArrayList<>();
    public TreeListAdapter(Context context) {
        this.context = context;
    }
    public void setTreeList(List<Tree> treeList) {
        this.treeList = treeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TreeListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.recycler_row, parent, false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeListAdapter.MyViewHolder holder, int position) {
        holder.tvLatitude.setText(this.treeList.get(position).latitudeNum);
        holder.tvLongitude.setText(this.treeList.get(position).longitudeNum);
        holder.tvIdNum.setText(this.treeList.get(position).idNum);
        holder.tvDiameterNum.setText(this.treeList.get(position).diameterNum);
        holder.tvSpeciesInfo.setText(this.treeList.get(position).speciesInfo);
    }

    @Override
    public int getItemCount() {
        return  this.treeList.size();
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
