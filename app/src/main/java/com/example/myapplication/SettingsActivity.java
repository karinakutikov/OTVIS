package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import org.json.JSONException;
import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private JSONObject configValues = new JSONObject();
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ImageView backToHomeBtn = findViewById(R.id.back_to_home_btn);
        backToHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("configurationTransfer", configValues.toString());
                finish();
            }
        });

        Button setDefaultBtn = findViewById(R.id.set_default_button);
        setDefaultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefaultValues();
            }
        });

        sharedPref = getSharedPreferences("mySettings", MODE_PRIVATE);
        editor = sharedPref.edit();

        initializeFragments(savedInstanceState);
        setupClickListener();

        loadConfig();
        Log.d("configurationDefault", configValues.toString());
    }

    private void setupClickListener() {
        // Handle the about button
        Button aboutBtn = findViewById(R.id.about_btn);
        aboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aboutFragment.aboutBox.getVisibility() == View.GONE) {
                    aboutFragment.aboutBox.setVisibility(View.VISIBLE);
                }
                else {
                    aboutFragment.aboutBox.setVisibility(View.GONE);
                }
            }
        });
    }

    // Initialize fragments in the layout
    private void initializeFragments(Bundle savedInstanceState) {
        aboutFragment = new AboutFragment();
    }

    private void loadValueToEditText(EditText editText, String preferenceKey, float defaultValue) throws JSONException {
        float savedValue = sharedPref.getFloat(preferenceKey, defaultValue);
        editText.setText(String.valueOf(savedValue));
        configValues.put(preferenceKey, savedValue);
        setupConfig(editText, preferenceKey);
    }

    private void loadValueToEditText(EditText editText, String preferenceKey, int defaultValue) throws JSONException {
        int savedValue = sharedPref.getInt(preferenceKey, defaultValue);
        editText.setText(String.valueOf(savedValue));
        configValues.put(preferenceKey, savedValue);
        setupConfig(editText, preferenceKey);
    }

    private void setDefaultValues() {
        setDefaultValueForEditText(R.id.threshold, "threshold", 0.6f);
        setDefaultValueForEditText(R.id.snow_height, "snowHeight", 0f);
        setDefaultValueForEditText(R.id.pause_threshold, "pauseThresh", 20);
        setDefaultValueForEditText(R.id.waiting_time, "waitingTime", 30);
        setDefaultValueForEditText(R.id.walking_distance, "walkingDist", 3);
        setDefaultValueForEditText(R.id.camera_fps, "cameraFps", 60);
    }

    private void setDefaultValueForEditText(int editTextId, String preferenceKey, float defaultValue) {
        EditText editText = findViewById(editTextId);
        editText.setText(String.valueOf(defaultValue));
        saveValueToSharedPreferences(editText, preferenceKey, defaultValue);
    }

    private void setDefaultValueForEditText(int editTextId, String preferenceKey, int defaultValue) {
        EditText editText = findViewById(editTextId);
        editText.setText(String.valueOf(defaultValue));
        saveValueToSharedPreferences(editText, preferenceKey, defaultValue);
    }

    private void saveValueToSharedPreferences(EditText editText, String preferenceKey, float defaultValue) {
        float value = Float.parseFloat(editText.getText().toString());
        editor.putFloat(preferenceKey, value);
        editor.apply();

        try {
            configValues.put(preferenceKey, value);
            editor.putString("setting_json", configValues.toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveValueToSharedPreferences(EditText editText, String preferenceKey, int defaultValue) {
        int value = Integer.parseInt(editText.getText().toString());
        editor.putInt(preferenceKey, value);
        editor.apply();

        try {
            configValues.put(preferenceKey, value);
            editor.putString("setting_json", configValues.toString());
            editor.apply();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfig() {
        try {
            loadValueToEditText(findViewById(R.id.threshold), "threshold", 0.6f);
            loadValueToEditText(findViewById(R.id.snow_height), "snowHeight", 0f);
            loadValueToEditText(findViewById(R.id.pause_threshold), "pauseThresh", 20);
            loadValueToEditText(findViewById(R.id.waiting_time), "waitingTime", 30);
            loadValueToEditText(findViewById(R.id.walking_distance), "walkingDist", 3);
            loadValueToEditText(findViewById(R.id.camera_fps), "cameraFps", 60);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupConfig(EditText editText, String preferenceKey) throws JSONException {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newValue = charSequence.toString();
                if (!newValue.isEmpty()) {
                    if (editText.getInputType() == InputType.TYPE_CLASS_NUMBER) {
                        int intValue = Integer.parseInt(newValue);
                        saveValueToSharedPreferences(editText, preferenceKey, intValue);
                    } else {
                        float floatValue = Float.parseFloat(newValue);
                        saveValueToSharedPreferences(editText, preferenceKey, floatValue);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}