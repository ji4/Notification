package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class EventEditor extends AppCompatActivity {
    private Context m_context;
    //widgets
    private NumberPicker m_numPicker_hour, m_numPicker_min, m_numPicker_am_pm;
    private RadioGroup m_radioGroup;
    private CheckBox m_chk_sound;
    private CheckBox m_chk_countdown;
    private ArrayList<Integer> m_iArryltNotificationRadioBtn;
    private Button m_btn_confirm;
    //values
    private int m_iEventId;
    private int m_iHour, m_iMin, m_iAm_pm, m_iNotification, m_iSound, m_iCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        findViews();
        init();
        getIntentData();
        setStoredData();

        m_chk_countdown.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    switchToCountdownNumPicker();
                } else {
                    switchToTimeNumPicker();
                }
            }
        });

        m_btn_confirm.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveEventData();
                updateDataOnHomePage();
                startNotifyService();
                finish();
            }
        });
    }

    public void onNotificationTypeRadioBtnClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        int iNotificationRadioBtnSize = m_iArryltNotificationRadioBtn.size();
        for (int i = 0; i < iNotificationRadioBtnSize; i++) {
            if (view.getId() == m_iArryltNotificationRadioBtn.get(i)) {
                if (checked) {
                    m_iNotification = i;
                    break;
                }
            }
        }
    }

    private void init() {
        /*shared prefrences*/
        m_context = this;
        KeyValueDB.getPrefs(m_context);

        switchToTimeNumPicker();

        /*notification radio button*/
        m_radioGroup.check(m_iArryltNotificationRadioBtn.get(0));
        /*sound effect checkbox*/
        m_chk_sound.setChecked(true);
    }

    private void getIntentData() {
        Intent it = getIntent();
        m_iEventId = it.getIntExtra(Constants.EVENT_ID, -1);
    }

    private void setStoredData() {
        //set stored data
        String strEventData = KeyValueDB.getEventData(m_context, String.valueOf(m_iEventId));
        if (m_iEventId != -1 && !strEventData.equals(KeyValueDB.NO_DATA)) {
            ArrayList<Integer> iArrltEventData = DataConverter.convertToIntArray(strEventData);
            m_iHour = iArrltEventData.get(0);
            m_iMin = iArrltEventData.get(1);
            m_iAm_pm = iArrltEventData.get(2);
            m_iNotification = iArrltEventData.get(3);
            m_iSound = iArrltEventData.get(4);
            m_iCountdown = iArrltEventData.get(5);

            m_numPicker_hour.setValue(m_iHour);
            m_numPicker_min.setValue(m_iMin);
            m_numPicker_am_pm.setValue(m_iAm_pm);
            m_radioGroup.check(m_iArryltNotificationRadioBtn.get(m_iNotification));
            if (m_iSound == 1) {
                m_chk_sound.setChecked(true);
            } else {//0
                m_chk_sound.setChecked(false);
            }

//            if (m_iCountdown == 1) {
//                m_chk_countdown.setChecked(true);
//            } else { //0
//                m_chk_countdown.setChecked(false);
//            }
        }
    }

    private void saveEventData() {
        //event code
        String strEventId = String.valueOf(m_iEventId);
        //event time
        int iHour = m_numPicker_hour.getValue();
        int iMin = m_numPicker_min.getValue();
        int iAm_pm = m_numPicker_am_pm.getValue();
        String strHour = String.valueOf(iHour);
        String strMin = String.valueOf(iMin);
        String strAm_pm = String.valueOf(iAm_pm);
        //event notification type
        String strNotificationType = String.valueOf(m_iNotification);
        //event sound effect
        String strPlaySound = m_chk_sound.isChecked() ? "1" : "0";
        //event numPicker time/countdown
        Boolean isCountDownChecked = m_chk_countdown.isChecked();
        String strCountDownChecked = isCountDownChecked ? "1" : "0";

        Calendar curCalendar = Calendar.getInstance();
        Calendar scheduledCalendar = (Calendar) curCalendar.clone();
        String strSec = "0";
        int iSec;
        if (isCountDownChecked) {
            iSec = iAm_pm;
            scheduledCalendar.add(Calendar.HOUR, iHour);
            scheduledCalendar.add(Calendar.MINUTE, iMin);
            scheduledCalendar.add(Calendar.SECOND, iSec);

            strHour = String.valueOf(scheduledCalendar.get(Calendar.HOUR));
            strMin = String.valueOf(scheduledCalendar.get(Calendar.MINUTE));
            strSec = String.valueOf(scheduledCalendar.get(Calendar.SECOND));
            strAm_pm = String.valueOf(scheduledCalendar.get(Calendar.AM_PM));
        }

        KeyValueDB.setEventData(m_context, strEventId, strHour + "," + strMin + "," + strSec + "," + strAm_pm + "," + strNotificationType + "," + strPlaySound + "," + strCountDownChecked);
        KeyValueDB.saveEventId(m_context, m_iEventId);
    }

    private void switchToCountdownNumPicker() {
        /*mumber pickers*/
        //hour
        m_numPicker_hour.setMinValue(0);
        m_numPicker_hour.setMaxValue(12);
        m_numPicker_hour.setValue(0);

        //minute
        m_numPicker_min.setMinValue(0);
        m_numPicker_min.setMaxValue(59);
        m_numPicker_min.setValue(0);

        //AM/PM
        m_numPicker_am_pm.setDisplayedValues(null);
        m_numPicker_am_pm.setMinValue(0);
        m_numPicker_am_pm.setMaxValue(59);
        m_numPicker_am_pm.setValue(3);
    }

    private void switchToTimeNumPicker() {
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
    }

    private void updateDataOnHomePage() {
        Intent it = new Intent();
        it.putExtra(Constants.EVENT_ID, m_iEventId);
        setResult(RESULT_OK, it);
    }

    private void startNotifyService() {
        Intent serviceIntent = new Intent(EventEditor.this, NotifyService.class);
        startService(serviceIntent);
    }

    private void findViews() {
        m_chk_countdown = (CheckBox) findViewById(R.id.activity_event_editor_chk_countdown);
        m_numPicker_hour = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_hour);
        m_numPicker_min = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_min);
        m_numPicker_am_pm = (NumberPicker) findViewById(R.id.activity_event_editor_numberPicker_am_pm);
        m_radioGroup = (RadioGroup) findViewById(R.id.activity_event_editor_radioGroup);
        m_chk_sound = (CheckBox) findViewById(R.id.activity_event_editor_chk_sound);
        m_btn_confirm = (Button) findViewById(R.id.activity_edit_event_btn_confirm);

        m_iArryltNotificationRadioBtn = new ArrayList<>(Arrays.asList(
                R.id.activity_event_editor_rb_simple,
                R.id.activity_event_editor_rb_bigText,
                R.id.activity_event_editor_rb_inbox,
                R.id.activity_event_editor_rb_bigPicture,
                R.id.activity_event_editor_rb_progress));
    }
}
