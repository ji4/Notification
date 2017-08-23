package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class ClockHomePage extends AppCompatActivity {
    private Button m_btn_time;
    private static final int EVENT_EDITOR = 0;
    private Context m_context;
    private ArrayList<Button> m_eventButtons;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EVENT_EDITOR) {
            if (resultCode == RESULT_OK) {
                int iEventId = data.getIntExtra(Constants.EVENT_ID, -1);
                setTimeOnButtonText(iEventId, m_eventButtons.get(iEventId));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_home_page);
        findViews();
        setButtonListener();
        initButtonText();
    }

    Button.OnClickListener btnForTime = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int iEventId = -1;
            switch (view.getId()) {
                case R.id.activity_clock_home_btn_time:
                    iEventId = 0;
                    break;
            }
            if (iEventId != -1) {
                Intent it = new Intent(ClockHomePage.this, EventEditor.class);
                it.putExtra(Constants.EVENT_ID, iEventId);
                startActivityForResult(it, EVENT_EDITOR);
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
            ArrayList<Integer> iArrltEventData = DataConverter.convertEventDataToInt(strEventData);
            String strHour = String.valueOf(iArrltEventData.get(0));
            String strMin = String.valueOf(iArrltEventData.get(1));
            String strAm_pm = String.valueOf(iArrltEventData.get(2));
            eventButton.setText(strHour + " : " + strMin + " " + (strAm_pm == "0" ? "AM" : "PM"));
        }
    }


    private void findViews() {
        m_btn_time = (Button) findViewById(R.id.activity_clock_home_btn_time);
    }

    private void setButtonListener() {
        m_btn_time.setOnClickListener(btnForTime);
    }
}
