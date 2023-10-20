package com.example.myapplication;


public interface UIUpdater {
    void addTreeMarker(double lat, double lon, int zoom, String treeID, double diameter, String species);
    void deleteAllMarkers();
    void showToast(String text);
    void updateGPSLight(String line);
    void updateMemory(String line);
    void updateConsole(String line, StringBuilder sb);
    void updateConsole();
    void updateTimer(String line);
    void updateTimer();
    void updateCamLocation(String line);
    void updateTreeLocation(String line);
    void setTreeDataHandler(TreeDataHandlerImpl treeDataHandler);
    void addCamMarker(double lat, double lon, float rotationDegree);
}
