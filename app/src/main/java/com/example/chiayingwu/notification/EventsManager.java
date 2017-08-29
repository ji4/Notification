package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Arrays;

public class EventsManager extends AppCompatActivity {
    private Button m_btn_time, m_btn_time2, m_btn_time3;
    private CheckBox m_checkBox, m_checkBox2, m_checkBox3;
    private static final int EVENT_EDITOR = 0;
    private Context m_context;
    private ArrayList<Button> m_arrEventButtons;
    private ArrayList<CheckBox> m_cbxArrltEvent;
    private ArrayList<Integer> m_iArrEventCheckBoxRId, m_iArrEventButtonRId;
    private KeyValueDB m_keyValueDB;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_EDITOR) {
            if (resultCode == RESULT_OK) {
                int iEventId = data.getIntExtra(Constants.KEY_EVENT_ID, -1);
                setTimeOnButtonText(iEventId, m_arrEventButtons.get(iEventId));
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
            int iEventButtonRIdSize = m_iArrEventButtonRId.size();
            int iEventId = -1;
            for (int i = 0; i < iEventButtonRIdSize; i++) {
                if (view.getId() == m_iArrEventButtonRId.get(i)) {
                    iEventId = i;
                }
                if (iEventId != -1) {
                    Intent it = new Intent(EventsManager.this, EventEditor.class);
                    it.putExtra(Constants.KEY_EVENT_ID, iEventId);
                    startActivityForResult(it, EVENT_EDITOR);
                    break;
                }
            }
        }
    };

    CompoundButton.OnCheckedChangeListener checkBoxListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            int iEventCheckBoxSize = m_iArrEventCheckBoxRId.size();
            for (int i = 0; i < iEventCheckBoxSize; i++) {
                if (compoundButton.getId() == m_iArrEventCheckBoxRId.get(i)) {
                    if (isChecked) {
                        m_keyValueDB.saveEventId(m_context, i);
                        startNotifyService();
                    } else {
                        m_keyValueDB.deleteEvent(m_context, i);
                        startNotifyService();
                    }
                    break;
                }
            }
        }
    };


    private void initButtonText() {
        /*shared prefrences*/
        m_context = this;
        m_keyValueDB = new KeyValueDB();
        m_keyValueDB.getPrefs(m_context);

        int iEventButtonsSize = m_arrEventButtons.size();
        for (int iEventId = 0; iEventId < iEventButtonsSize; iEventId++) {
            setTimeOnButtonText(iEventId, m_arrEventButtons.get(iEventId));
        }
    }

    private void setTimeOnButtonText(int iEventId, Button eventButton) {
        //set stored data
        String strEventData = m_keyValueDB.getEventData(m_context, String.valueOf(iEventId));
        if (iEventId != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            ArrayList<Integer> iArrltEventData = DataConverter.convertToIntArray(strEventData);
            String strHour = String.valueOf(iArrltEventData.get(0));
            String strMin = String.valueOf(iArrltEventData.get(1));
            String strAm_pm = String.valueOf(iArrltEventData.get(3));
            eventButton.setText(strHour + " : " + strMin + " " + (strAm_pm == "0" ? "AM" : "PM"));
        }
    }

    private void startNotifyService() {
        Intent serviceIntent = new Intent(EventsManager.this, NotifyService.class);
        startService(serviceIntent);
    }

    private void findViews() {
        /*buttons*/
        m_btn_time = (Button) findViewById(R.id.activity_events_manager_btn_time);
        m_btn_time2 = (Button) findViewById(R.id.activity_events_manager_btn_time2);
        m_btn_time3 = (Button) findViewById(R.id.activity_events_manager_btn_time3);
        m_arrEventButtons = new ArrayList<>(Arrays.asList(m_btn_time, m_btn_time2, m_btn_time3));
        m_iArrEventButtonRId = new ArrayList<>(Arrays.asList(
                R.id.activity_events_manager_btn_time,
                R.id.activity_events_manager_btn_time2,
                R.id.activity_events_manager_btn_time3
        ));


        /*checkbox*/
        m_checkBox = (CheckBox) findViewById(R.id.activity_events_manager_checkBox);
        m_checkBox2 = (CheckBox) findViewById(R.id.activity_events_manager_checkBox2);
        m_checkBox3 = (CheckBox) findViewById(R.id.activity_events_manager_checkBox3);

        m_cbxArrltEvent = new ArrayList<>(Arrays.asList(
                m_checkBox, m_checkBox2, m_checkBox3
        ));
        m_iArrEventCheckBoxRId = new ArrayList<>(Arrays.asList(
                R.id.activity_events_manager_checkBox,
                R.id.activity_events_manager_checkBox2,
                R.id.activity_events_manager_checkBox3
        ));
    }

    private void setButtonListener() {
        int iEventButtonsSize = m_arrEventButtons.size();
        for (int i = 0; i < iEventButtonsSize; i++) {
            m_arrEventButtons.get(i).setOnClickListener(btnForTime);
        }

        int iCbxEventSize = m_cbxArrltEvent.size();
        for (int i = 0; i < iCbxEventSize; i++) {
            m_cbxArrltEvent.get(i).setOnCheckedChangeListener(checkBoxListener);
        }
    }
}
