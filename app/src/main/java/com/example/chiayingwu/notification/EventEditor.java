package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;

public class EventEditor extends AppCompatActivity {
    private NumberPicker m_numPicker_hour, m_numPicker_min, m_numPicker_am_pm;
    private Button m_btn_confirm;
    private RadioGroup m_radioGroup;

    private int m_iEventCode;
    private int m_iHour, m_iMin, m_iAm_pm, m_iSound, m_iNotification;
    private int m_iCheckedNotificatioinType;
    private Context m_context;
    private ArrayList<Integer> m_iArryltNotificationRadioBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        findViews();
        init();
        getIntentData();
        setStoredData();

        m_btn_confirm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEventData();
                updateDataOnHomePage();
                finish();
            }
        });


    }

    private void init() {
        /*shared prefrences*/
        m_context = this;
        KeyValueDB.getPrefs(m_context);

        /*mumber pickers*/
        //hour
        m_numPicker_hour.setMinValue(1);
        m_numPicker_hour.setMaxValue(12);
        m_numPicker_hour.setValue(1);

        //minute
        m_numPicker_min.setMinValue(0);
        m_numPicker_min.setMaxValue(59);
        m_numPicker_min.setValue(1);

        //AM/PM
        m_numPicker_am_pm.setMinValue(0);
        m_numPicker_am_pm.setMaxValue(1);
        m_numPicker_am_pm.setDisplayedValues(new String[]{"AM", "PM"});
        m_numPicker_am_pm.setValue(1);

        /*radio button*/
        m_radioGroup.check(m_iArryltNotificationRadioBtn.get(0));
    }

    private void getIntentData() {
        Intent it = getIntent();
        m_iEventCode = it.getIntExtra("eventCode", -1);
    }

    private void setStoredData() {
        //set stored data
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(m_iEventCode));
        if (m_iEventCode != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            ArrayList<Integer> iArrltEventData = DataConverter.convertEventDataToInt(strEventData);
            m_iHour = iArrltEventData.get(0);
            m_iMin = iArrltEventData.get(1);
            m_iAm_pm = iArrltEventData.get(2);
//            m_iSound = iArrltEventData.get(3);
//            m_iNotification = iArrltEventData.get(4);

            m_numPicker_hour.setValue(m_iHour);
            m_numPicker_min.setValue(m_iMin);
            m_numPicker_am_pm.setValue(m_iAm_pm);
//            m_radioGroup.check(m_iArryltNotificationRadioBtn.get(m_iNotification));
        }
    }

    public void onNotificationTypeRadioBtnClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        for (int i = 0; i < m_iArryltNotificationRadioBtn.size(); i++) {
            if (view.getId() == m_iArryltNotificationRadioBtn.get(i)) {
                if (checked) {
                    m_iCheckedNotificatioinType = i;
                }
            }
        }
    }

    private void saveEventData(){
        String strEventCode = String.valueOf(m_iEventCode);
        String strHour = String.valueOf(m_numPicker_hour.getValue());
        String strMin = String.valueOf(m_numPicker_min.getValue());
        String strAm_pm = String.valueOf(m_numPicker_am_pm.getValue());
        KeyValueDB.setEventData(m_context, strEventCode, strHour + "," + strMin + "," + strAm_pm);
    }

    private void updateDataOnHomePage() {
        Intent it = new Intent();
        it.putExtra("eventCode", m_iEventCode);
        setResult(RESULT_OK, it);
    }

    private void findViews() {
        m_numPicker_hour = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_hour);
        m_numPicker_min = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_min);
        m_numPicker_am_pm = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_am_pm);
        m_radioGroup = (RadioGroup) findViewById(R.id.activity_event_editor_radioGroup);
        m_btn_confirm = (Button) findViewById(R.id.activity_edit_event_btn_confirm);

        m_iArryltNotificationRadioBtn = new ArrayList<>(Arrays.asList(
                R.id.activity_event_editor_rb_simple,
                R.id.activity_event_editor_rb_bigText,
                R.id.activity_event_editor_rb_inbox,
                R.id.activity_event_editor_rb_bigPicture,
                R.id.activity_event_editor_rb_progress));
    }
}
