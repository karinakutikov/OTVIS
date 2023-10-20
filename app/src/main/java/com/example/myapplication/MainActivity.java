package com.example.myapplication;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MapFragment.OnMapFragmentReadyListener{
    private final String HOST = "192.168.1.1";
    private final int PORT = 7172;

    // UI elements
    private TextView memoryUsage, timeRemaining, hdopView;
    private SocketConnection socketConnection;
    private Button startBtn;
    private ImageView gpsLight;
    private LogsFragment logsFragment;
    private DatabaseFragment databaseFragment;
    private InfoHandler infoHandler;
    private SharedPreferences sharedPref;
    private String settingsJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        settingsJson = sharedPref.getString("setting_json", "{}");

        // Initialize fragments and set up click listeners
        initializeFragments(savedInstanceState);
        setupClickListeners();
        Log.d("configurationCreate", settingsJson);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Set selected item in bottom navigation bar
        NavigationBarView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        if (databaseFragment.databaseBox.getVisibility() == View.VISIBLE) {
            databaseFragment.databaseBox.setVisibility(View.GONE);
        }

        // Load settings from shared preferences
        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        settingsJson = sharedPref.getString("setting_json", "{}");
        Log.d("configurationResume", settingsJson);
    }

    // Connect to the server
    private void connectToServer(){
        startBtn.setText("Stop");
        try {
            // connect to the server in a background thread
            socketConnection = new SocketConnection(infoHandler, MainActivity.this, settingsJson);
            socketConnection.connectToServer(HOST, PORT);
            startBtn.setBackgroundResource(android.R.color.transparent);
            startBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.stop_icon, 0, 0);
        } catch (Exception e) {
            Log.e("MainActivity", "Error connecting to server", e);
            Toast.makeText(MainActivity.this, "Failed to connect to server.", Toast.LENGTH_SHORT).show();
        }
    }

//    Disconnect to the server
    private void disconnectToServer(){
        socketConnection.disconnectFromServerInThread();
        infoHandler.showToast("Disconnecting...");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startBtn.setText("Start");
                startBtn.setBackgroundResource(android.R.color.transparent);
                startBtn.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.start_icon, 0, 0);
                memoryUsage.setText("Memory: NA");
                if (logsFragment != null) {
                    String currentText = logsFragment.getLogsText();
                    String newText = currentText + "\nSearching stopped........................\n";
                    infoHandler.updateConsole(newText, new StringBuilder());
                }
            }
        }, 3000);
    }


    // Initialize fragments in the layout
    private void initializeFragments(Bundle savedInstanceState) {
        MapFragment mapFragment = new MapFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_map_container, mapFragment).commit();

        logsFragment = new LogsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_logs_container, logsFragment).commit();

        if (savedInstanceState == null) {
            databaseFragment = new DatabaseFragment(new TreeDataHandlerImpl(new ViewModelProvider(this).get(TreeViewModel.class)));
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_database_container, databaseFragment).commit();
        } else {
            databaseFragment = (DatabaseFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_database_container);
        }
    }

    // Callback when MapFragment is ready
    @Override
    public void onMapFragmentReady(GoogleMap mMap) {
        // The map is ready at this point. You can now initialize UIUpdater and InfoHandler.
        initializeUIComponents(mMap);
    }

    private void initializeUIComponents(GoogleMap mMap) {
        // TODO: Wait until mMap is ready before initializing UIUpdater and InfoHandler
        // Currently, this will result in mMap being null.
        TreeViewModel treeViewModel = new ViewModelProvider(this).get(TreeViewModel.class);
        TreeDataHandlerImpl treeDataHandler = new TreeDataHandlerImpl(treeViewModel);

        timeRemaining = findViewById(R.id.remaining_time);
        memoryUsage = findViewById(R.id.memory_usage);
        gpsLight = findViewById(R.id.gps_light);
        hdopView = findViewById(R.id.hdop_value);

        // Initialize UIUpdater with references to UI elements
        UIUpdater uiUpdater = new UIUpdaterImpl(mMap, this, gpsLight, logsFragment.systemLogs, memoryUsage, timeRemaining, hdopView);
        uiUpdater.setTreeDataHandler(treeDataHandler);

        // Initialize InfoHandler to manage UI updates and messages
        infoHandler = new InfoHandler(uiUpdater, this);

        infoHandler.updateConsole("", new StringBuilder());
        infoHandler.updateTimer("");
        timeRemaining.setVisibility(View.GONE);
    }

    private void setupClickListeners() {
        // click the start button and connect to the server
        startBtn = findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Connect to the server
                if (startBtn.getText().equals("Start")){
                    connectToServer();
                } else {
                    disconnectToServer();
                    Log.d("MainActivity", "setupClickListeners");
                }
            }
        });

        // Handle the delete button
        Button deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoHandler.deleteAllMarkers();
                infoHandler.showToast("All markers deleted");
            }
        });

        // Handle the log button
        Button logsBtn = findViewById(R.id.logs_btn);
        logsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (logsFragment.sysLogsBox.getVisibility() == View.GONE) {
                    logsFragment.sysLogsBox.setVisibility(View.VISIBLE);
                } else {
                    logsFragment.sysLogsBox.setVisibility(View.GONE);
                }
            }
        });

        // Handle the navigation bar
        NavigationBarView bottomNavigationView = findViewById(R.id.navigation_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(
            new NavigationBarView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home){
                        if (databaseFragment.databaseBox.getVisibility() == View.VISIBLE) {
                            databaseFragment.databaseBox.setVisibility(View.GONE);
                        }
                    } else if (item.getItemId() == R.id.database) {
                        // Open the database fragment
                        if (databaseFragment.databaseBox.getVisibility() == View.GONE) {
                            databaseFragment.databaseBox.setVisibility(View.VISIBLE);
                        }
                    } else if (item.getItemId() == R.id.setting) {
                        if (databaseFragment.databaseBox.getVisibility() == View.VISIBLE) {
                            databaseFragment.databaseBox.setVisibility(View.GONE);
                        }
                        // Navigate to Setting Activity
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
            }
        );
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Disconnect from the server if the activity is destroyed
        socketConnection.disconnectFromServerInThread();
    }
}