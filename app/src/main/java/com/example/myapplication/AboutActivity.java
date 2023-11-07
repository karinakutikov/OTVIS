package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONObject;

public class AboutActivity extends AppCompatActivity {
    private JSONObject configValues = new JSONObject();
    protected ScrollView aboutScrollView;
    protected ConstraintLayout aboutBox;
    protected TextView about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView backToSettingsBtn = findViewById(R.id.back_to_settings_btn);
        backToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("configurationTransfer", configValues.toString());
                finish();
            }
        });
    }

}



