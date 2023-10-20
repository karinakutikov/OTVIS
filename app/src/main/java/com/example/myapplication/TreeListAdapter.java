package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.db.Tree;

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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvIdNum;
        TextView tvLatitude;
        TextView tvLongitude;
        TextView tvDiameterNum;
        TextView tvSpeciesInfo;

        public MyViewHolder(View view) {
            super(view);
            tvIdNum = view.findViewById(R.id.tvIdNum);
            tvLatitude = view.findViewById(R.id.tvLatitude);
            tvLongitude = view.findViewById(R.id.tvLongitude);
            tvDiameterNum = view.findViewById(R.id.tvDiameterNum);
            tvSpeciesInfo = view.findViewById(R.id.tvSpeciesInfo);

        }
    }
}
