package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.tasks.Task;


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LinearLayout destinationLayout;
    private EditText latitudeInput;
    private EditText longitudeInput;
    private final int zoom = 20;
    private OnMapFragmentReadyListener callbackListener;

    public interface OnMapFragmentReadyListener {
        void onMapFragmentReady(GoogleMap mMap);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            callbackListener = (OnMapFragmentReadyListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnMapFragmentReadyListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container,false);

        initializeMap();
        initializeUIComponents(view);

        return view;
    }
    private void initializeMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        if (supportMapFragment != null) {
            supportMapFragment.getMapAsync(this);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }


    private void initializeUIComponents(View view) {
        Button cacheMapButton = view.findViewById(R.id.cache_map_btn);
        destinationLayout = view.findViewById(R.id.destination_layout);
        latitudeInput = view.findViewById(R.id.latitude_input);
        longitudeInput = view.findViewById(R.id.longitude_input);
        Button goButton = view.findViewById(R.id.go_button);
        Button myLocBtn = view.findViewById(R.id.location_btn);

        cacheMapButton.setOnClickListener(v -> toggleDestinationLayout());
        goButton.setOnClickListener(v -> moveToSpecifiedLocation());
        myLocBtn.setOnClickListener(v -> moveToCurrentLocation());
    }

    private void toggleDestinationLayout() {
        if (destinationLayout.getVisibility() == View.VISIBLE) {
            destinationLayout.setVisibility(View.GONE);
        } else {
            destinationLayout.setVisibility(View.VISIBLE);
        }
    }


    private void moveToSpecifiedLocation() {
        if (latitudeInput.getText().toString().isEmpty() || longitudeInput.getText().toString().isEmpty()) {
            showToast("Please enter valid number!", Gravity.TOP | Gravity.CENTER, 0, 80);
        } else {
            double latitude = Double.parseDouble(latitudeInput.getText().toString());
            double longitude = Double.parseDouble(longitudeInput.getText().toString());
            LatLng destination = new LatLng(latitude, longitude);

            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, zoom));
            }
            latitudeInput.getText().clear();
            longitudeInput.getText().clear();
            destinationLayout.setVisibility(View.GONE);
        }
    }


    @SuppressLint("MissingPermission")
    protected void moveToCurrentLocation(){
        if (hasLocationPermission()) {
            Task<Location> task = fusedLocationProviderClient.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
                } else {
                    showToast("Current location is null. Using defaults.", Gravity.TOP | Gravity.CENTER, 0, 80);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(45.3528, -75.7885), zoom));
                }
            });
        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
        }
    }


    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void showToast(String message, int gravity, int xOffset, int yOffset) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                moveToCurrentLocation();
            }else {
                showToast("Location permission is denied, please allow the permission", Gravity.TOP | Gravity.CENTER, 0, 80);
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        moveToCurrentLocation();

//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.setPadding(0, 800, 0, 100);

        if (callbackListener != null) {
            callbackListener.onMapFragmentReady(mMap);
        }
    }
}