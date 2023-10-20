package com.example.myapplication;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogsFragment extends Fragment {
    protected ScrollView sysLogsScrollView;
    protected ConstraintLayout sysLogsBox;
    protected TextView systemLogs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container,false);
        sysLogsBox = view.findViewById(R.id.sys_logs_box);
        sysLogsScrollView = view.findViewById(R.id.scroll_view_sys);
        systemLogs = view.findViewById(R.id.sys_logs);

        setupSystemLogs();

        // Inflate the layout for this fragment
        return view;
    }

    public String getLogsText(){
        TextView sysLogs = getView().findViewById(R.id.sys_logs);
        return sysLogs.getText().toString();
    }

    private void setupSystemLogs() {
        systemLogs.setMovementMethod(new ScrollingMovementMethod());
        systemLogs.addTextChangedListener(new SystemLogsTextWatcher());
    }

    private class SystemLogsTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // This method is called before the text is changed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // This method is called when the text is changing
        }

        @Override
        public void afterTextChanged(Editable s) {
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
        sysLogsScrollView.post(() -> sysLogsScrollView.scrollTo(0, sysLogsScrollView.getBottom()));
    }
}