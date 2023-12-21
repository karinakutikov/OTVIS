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

import java.util.ArrayList;
import java.util.List;

public class TreeListAdapter extends RecyclerView.Adapter<TreeListAdapter.MyViewHolder> {

    private Context context;
    RecyclerView recyclerView;
    private List<Tree> treeList = new ArrayList<>();
    public TreeListAdapter(Context context) {
        this.context = context;
        //setListener((new )this);
        //TreeListAdapter adapter = new TreeListAdapter(context);
        //setListener((OnItemClickListener) this);
        //recyclerView.setAdapter(adapter);
    }

    public void setTreeList(List<Tree> treeList) {
        Tree firstTree = new Tree(); // create a new tree object
        treeList.add(firstTree); // add it to the list
        firstTree.idNum = "1"; // modify the idNum field of the tree
        firstTree.latitudeNum = "45.3528"; // modify the latitudeNum field of the tree
        firstTree.longitudeNum = "-75.7885"; // modify the longitudeNum field of the tree
        firstTree.diameterNum = "10"; // modify the diameterNum field of the tree
        firstTree.speciesInfo = "Maple"; // modify the speciesInfo field of the tree
        this.treeList = treeList;
        notifyDataSetChanged();

        //TreeListAdapter adapter = new TreeListAdapter(context);
        //adapter.setListener((OnItemClickListener) this);
        //recyclerView.setAdapter(adapter);
    }
    //public void setListener(OnItemClickListener listener) {

        //TreeListAdapter.listener = listener;
    //}
//    public interface OnItemClickListener{
//        void onButtonClick();
//    }
    //OnItemClickListener listener = new OnItemClickListener() {
      //  @Override
        //public void onButtonClick() {

        //}
    //};

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

        public MyViewHolder(View view) {
            super(view);
            Log.e("TreeListAdapter", "MyViewHolder");
            tvIdNum = view.findViewById(R.id.tvIdNum);
            tvLatitude = view.findViewById(R.id.tvLatitude);
            tvLongitude = view.findViewById(R.id.tvLongitude);
            tvDiameterNum = view.findViewById(R.id.tvDiameterNum);
            tvSpeciesInfo = view.findViewById(R.id.tvSpeciesInfo);
            goBtn = view.findViewById(R.id.tvLocateTree);

//            MapFragment mapListener = new MapFragment();
//            Log.e("initialization", "log click");
//
//            goBtn.setOnClickListener(v -> {
//                Log.e("TreeListAdapter", "setOnClickListener");
//                if (mapListener != null){
//                    mapListener.moveToCurrentLocation();
//                }else{
//                    Log.e("TreeListAdapter", "Listener is null");
//                }

                //int position = getAdapterPosition();
                //Log.e("MyViewHolder", "Position: " + position);
                //if (position != RecyclerView.NO_POSITION && listener != null) {

                    //Log.e("TreeListAdapter", "!=null");
                //}else{
                    //Log.e("TreeListAdapter", "=null");
                //
            //});

        }
    }
}
