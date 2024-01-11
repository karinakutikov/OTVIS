package com.example.myapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SocketConnection {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    protected InfoHandler infoHandler;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private String settingJson;


    public SocketConnection(InfoHandler infoHandler, Context context, String settingsJson) {
        this.infoHandler = infoHandler;
        this.settingJson = settingsJson;
    }



    public void sendMessageToServer(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public void disconnectFromServer() throws IOException {
        sendMessageToServer("stop");
        Log.d("MainActivity", "Attempting to disconnect... in SocketConnection");
        if (in != null) {in.close();}
        if (out != null) {out.close();}
        if (socket != null){
//            socket.setSoLinger(true, 0);  // This helps in faster socket closure
            socket.close();
//            socket = null;
        }
        infoHandler.updateMemory("NA");
        infoHandler.updateTimer();

    }

    public void disconnectFromServerInThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    disconnectFromServer();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Handle the incoming data and update the UI
    private void handleIncomingData(String line, StringBuilder sb) {
        Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run(){
                infoHandler.updateConsole(line, sb);
                infoHandler.updateGPSLight(line);
                infoHandler.updateMemory(line);
                infoHandler.updateTimer(line);
                infoHandler.updateCamLocation(line);
                infoHandler.updateTreeLocation(line);
            }
        });
    }

    private class ConnectTask implements Runnable {
        private String host;
        private int port;

        ConnectTask(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(host, port);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Log.i("RunConnectTask", String.valueOf(socket.getInputStream()));

                infoHandler.updateConsole();
                infoHandler.showToast(settingJson);
                sendMessageToServer("configuration:: "+ settingJson);

                sendMessageToServer("start");
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    Log.d("RunConnectTask", line);
                    handleIncomingData(line, sb);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } /*finally {
                // Close the socket and any open streams
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                    if (socket != null) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/
        }
    }

    public void connectToServer(String host, int port){
        Log.d("MainActivity", "Attempting to connect... in SocketConnection");
        executor.execute(new ConnectTask(host, port));
    }
}
