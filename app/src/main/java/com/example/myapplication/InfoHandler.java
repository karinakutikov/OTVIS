package com.example.myapplication;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

public class InfoHandler {
    private final Handler handler;
    private final UIUpdater uiUpdater;


    public InfoHandler(UIUpdater uiUpdater, Context context) {
        this.handler = new Handler(Looper.getMainLooper());
        this.uiUpdater = uiUpdater;
    }


    public void deleteAllMarkers() {
        handler.post(() -> uiUpdater.deleteAllMarkers());
    }

    public void showToast(String text) {
        handler.post(() -> uiUpdater.showToast(text));
    }

    public void updateConsole(String line, StringBuilder sb){
        handler.post(() -> uiUpdater.updateConsole(line, sb));
    }
    public void updateConsole(){
        handler.post(uiUpdater::updateConsole);
    }

    public void updateGPSLight(String line){
        handler.post(() -> uiUpdater.updateGPSLight(line));
    }

    public void updateMemory(String line){
        handler.post(() -> uiUpdater.updateMemory(line));
    }

    public void updateTimer(String line){
        handler.post(() -> uiUpdater.updateTimer(line));
    }
    public void updateTimer(){handler.post(() -> uiUpdater.updateTimer());}
    public void updateCamLocation(String line){
        handler.post(() -> uiUpdater.updateCamLocation(line));
    }

    public void updateTreeLocation(String line){
        handler.post(() -> uiUpdater.updateTreeLocation(line));
    }

}
