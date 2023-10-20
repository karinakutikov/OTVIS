package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class UIUpdaterImpl implements UIUpdater {
    protected HashMap<String, Marker> markerMap;
    private final GoogleMap mMap;
    protected ArrayList<Marker> posMarker;
    private final Context context;
    private final TextView memoryUsage, timeRemaining, systemLogs, hdopView;
    private final ImageView gpsLight;
    protected TreeDataHandlerImpl treeDataHandler;
    private String HDOP = null;
    private Marker camMaker, initLocMaker;
    boolean addFirstCam = true;
    boolean firstTreeAppear = false;
    public UIUpdaterImpl(GoogleMap mMap, Context context, ImageView gpsLight, TextView systemLogs, TextView memoryUsage, TextView timeRemaining, TextView hdopView) {
        this.mMap = mMap;
        this.context = context;
        this.gpsLight = gpsLight;
        this.memoryUsage = memoryUsage;
        this.systemLogs = systemLogs;
        this.timeRemaining = timeRemaining;
        this.markerMap = new HashMap<>();
        this.posMarker = new ArrayList<>();
        this.camMaker = null;
        this.hdopView = hdopView;
    }

    @Override
    public void setTreeDataHandler(TreeDataHandlerImpl treeDataHandler) {
        this.treeDataHandler = treeDataHandler;
    }

    public void addTreeMarker(double lat, double lon, int zoom, String treeID, double diameter, String species) {
        if (markerMap.containsKey(treeID) && markerMap.get(treeID)!= null){
            markerMap.get(treeID).remove();
        }
        LatLng tree = new LatLng(lat, lon);
        Marker marker = mMap.addMarker(new MarkerOptions().position(tree).title("T" + treeID + ", Diameter:" + diameter + "cm, Species:" + species));
        markerMap.put(treeID, marker);

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }
    public void updateTreeLocation(String line){
        if (line.contains("ID:")) {
            firstTreeAppear = true;
            timeRemaining.setVisibility(View.GONE);
            String[] parts = line.split(",\\s+");
            String treeId = parts[0].substring(4);
            String lat = (parts[1].substring(5));
            String lon =(parts[2].substring(5));
            String diameter = (parts[3].substring(10));
            String species = parts[4].substring(8);
            addTreeMarker(Double.parseDouble(lat), Double.parseDouble(lon), 20, treeId, Double.parseDouble(diameter), species);
            treeDataHandler.receiveTreeData(treeId, lat, lon, diameter, species);
        }
    }

    public void addCamMarker(double lat, double lon, float rotationDegree) {
        LatLng myPos = new LatLng(lat, lon);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Bitmap arrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.camera_direction_icon);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotationDegree, arrowBitmap.getWidth() / 2f, arrowBitmap.getHeight() / 2f);
        Bitmap rotatedArrowBitmap = Bitmap.createBitmap(arrowBitmap, 0, 0, arrowBitmap.getWidth(), arrowBitmap.getHeight(), matrix, true);
        BitmapDescriptor cameraIcon = BitmapDescriptorFactory.fromBitmap(rotatedArrowBitmap);

        if (camMaker != null){
            camMaker.remove();
        }
        camMaker = mMap.addMarker(new MarkerOptions()
                    .position(myPos)
                    .title("Lat:" + lat + ", Lon:" + lon)
                    .icon(cameraIcon));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
    }

    public void updateCamLocation(String line){
        if (line.contains("Cam_lat")) {
            String[] parts = line.split(",\\s+");
            double myLat = Double.parseDouble(parts[0].substring(9));
            double myLon = Double.parseDouble(parts[1].substring(9));
            float rotationDegree = Float.parseFloat(parts[2].substring(9));
            Log.d("posMarker", line);

            addCamMarker(myLat, myLon, rotationDegree);

            timeRemaining.setVisibility(View.GONE);
            if (addFirstCam && firstTreeAppear){
                addFirstCam = false;
                addInitLocMarker(myLat, myLon);
            }
        }
    }

    public void addInitLocMarker(double lat, double lon) {
        LatLng myPos = new LatLng(lat, lon);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        BitmapDescriptor cameraIcon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.current_loc_icon));

        initLocMaker = mMap.addMarker(new MarkerOptions()
                .position(myPos)
                .title("Lat:" + lat + ", Lon:" + lon)
                .icon(cameraIcon));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(myPos));
        posMarker.add(initLocMaker);
    }


    public void deleteTreeMarkers() {
        Log.d("markerList", markerMap.toString());
        if (!markerMap.isEmpty()){
            for (Marker marker : markerMap.values()) {
                marker.remove();
            }
            markerMap.clear();
        }
    }
    public void deleteAllMarkers() {
        if (camMaker != null){
            camMaker.remove();
            camMaker = null;
        }

        if (initLocMaker != null){
            initLocMaker.remove();
            initLocMaker = null;
        }

        if (!posMarker.isEmpty()){
            Iterator<Marker> iterator = posMarker.iterator();
            while (iterator.hasNext()){
                Marker marker = iterator.next();
                iterator.remove();
                marker.remove();
            }
        }
        deleteTreeMarkers();
    }

    public void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP | Gravity.CENTER, 0, 80);
        toast.show();
    }


    public void updateGPSLight(String line){
        if (HDOP != null){
            hdopView.setText(HDOP);
            if (Double.parseDouble(HDOP) < 2f){
                gpsLight.setImageResource(R.drawable.gps_good);
            }else if (Double.parseDouble(HDOP) < 10f){
                gpsLight.setImageResource(R.drawable.gps_on);
            }else {
                gpsLight.setImageResource(R.drawable.gps_searching);
            }
        }
    }

    public void updateMemory(String line){
        if (line.contains("memory")){
            String memory = line.split(":")[1];
            memoryUsage.setText("Memory: "+memory);
        }
    }

    public void updateConsole(String line, StringBuilder sb){

        if (!line.trim().isEmpty() &&
            !line.contains("memory") &&
            !line.contains("user is not moving")&&
            !line.contains("No GPS")&&
            !line.contains("Time")&&
            !line.contains("walking")&&
            !line.contains("valid GPS readings") &&
            !line.contains("Cam_lat")) {
                sb.append(line).append("\n\n");
                systemLogs.setText(sb.toString());
        }


    }
    public void updateConsole(){
        systemLogs.setText(" ");
    }

    public void updateTimer(String line){
        if (line.contains("Time remaining")){
//            String[] parts = line.split(":");
            timeRemaining.setVisibility(View.VISIBLE);
            timeRemaining.setText(line);
        }
        if (line.contains("Start walking straight")){
            String[] parts = line.split(":\\s+");
            timeRemaining.setVisibility(View.VISIBLE);
            timeRemaining.setText(parts[1]);
        }
        if (line.contains("Initial reading")){
            String[] parts = line.split(",\\s+");
            HDOP = parts[0].split(":")[1];
            String lat = parts[1].split(": ")[1];
            String lon = parts[2].split(": ")[1];
            addInitLocMarker(Double.parseDouble(lat), Double.parseDouble(lon));;
            timeRemaining.setVisibility(View.GONE);
        }
    }
    public void updateTimer(){
        timeRemaining.setVisibility(View.GONE);
    }



}
