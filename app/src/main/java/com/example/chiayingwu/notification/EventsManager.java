package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class EventsManager extends AppCompatActivity {
    private Button m_btn_time;
    private CheckBox m_checkBox;
    private static final int EVENT_EDITOR = 0;
    private Context m_context;
    private ArrayList<Button> m_eventButtons;
    private ArrayList<CheckBox> m_cbxArrltEvent;
    private ArrayList<Integer> m_iArrEventCheckBoxes;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_EDITOR) {
            if (resultCode == RESULT_OK) {
                int iEventId = data.getIntExtra(Constants.EVENT_ID, -1);
                setTimeOnButtonText(iEventId, m_eventButtons.get(iEventId));
                m_cbxArrltEvent.get(iEventId).setChecked(true);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_manager);
        findViews();
        setButtonListener();
        initButtonText();
    }

    Button.OnClickListener btnForTime = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int iEventId = -1;
            switch (view.getId()) {
                case R.id.activity_events_manager_btn_time:
                    iEventId = 0;
                    break;
            }
            if (iEventId != -1) {
                Intent it = new Intent(EventsManager.this, EventEditor.class);
                it.putExtra(Constants.EVENT_ID, iEventId);
                startActivityForResult(it, EVENT_EDITOR);
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int iEventCheckBoxesSize = m_iArrEventCheckBoxes.size();
            for (int i = 0; i < iEventCheckBoxesSize; i++) {
                if (compoundButton.getId() == m_iArrEventCheckBoxes.get(i)) {
                    if (isChecked) {
                        Toast.makeText(m_context, "checked", Toast.LENGTH_SHORT).show();
                        KeyValueDB.saveEventId(m_context, i);
                        startNotifyService();
                    } else {
                        Toast.makeText(m_context, "unchecked", Toast.LENGTH_SHORT).show();
                        KeyValueDB.deleteExpiredEvent(m_context, i);
                        startNotifyService();
                    }
                }
            }
        }
    };


    private void initButtonText() {
        /*shared prefrences*/
        m_context = this;
        KeyValueDB.getPrefs(m_context);

        m_eventButtons = new ArrayList<>(Arrays.asList(m_btn_time));
        int iEventButtonsSize = m_eventButtons.size();
        for (int iEventId = 0; iEventId < iEventButtonsSize; iEventId++) {
            setTimeOnButtonText(iEventId, m_eventButtons.get(iEventId));
        }
    }

    private void setTimeOnButtonText(int iEventId, Button eventButton) {
        //set stored data
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(iEventId));
        if (iEventId != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            ArrayList<Integer> iArrltEventData = DataConverter.convertToIntArray(strEventData);
            String strHour = String.valueOf(iArrltEventData.get(0));
            String strMin = String.valueOf(iArrltEventData.get(1));
            String strAm_pm = String.valueOf(iArrltEventData.get(2));
            eventButton.setText(strHour + " : " + strMin + " " + (strAm_pm == "0" ? "AM" : "PM"));
        }
    }

    private void startNotifyService() {
        Intent serviceIntent = new Intent(EventsManager.this, NotifyService.class);
        startService(serviceIntent);
    }

    private void findViews() {
        m_btn_time = (Button) findViewById(R.id.activity_events_manager_btn_time);
        m_eventButtons = new ArrayList<>(Arrays.asList(m_btn_time));

        m_checkBox = (CheckBox) findViewById(R.id.activity_events_manager_checkBox);
        m_cbxArrltEvent = new ArrayList<>(Arrays.asList(
                m_checkBox
        ));
        m_iArrEventCheckBoxes = new ArrayList<>(Arrays.asList(
                R.id.activity_events_manager_checkBox
        ));
    }

    private void setButtonListener() {
        m_btn_time.setOnClickListener(btnForTime);

        m_checkBox.setOnCheckedChangeListener(checkBoxListener);
    }
}
