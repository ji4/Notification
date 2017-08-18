package com.example.chiayingwu.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ClockHomePage extends AppCompatActivity {
    private Button m_btn_time;
    private static final int requestCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_home_page);
        findViews();
        setButtonListener();
    }

    Button.OnClickListener btnForTime = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            int iEventCode = -1;
            switch (view.getId()) {
                case R.id.activity_clock_home_btn_time:
                    iEventCode = 0;
                    break;
            }
            if (iEventCode != -1) {
                Intent it = new Intent(ClockHomePage.this, EventEditor.class);
                it.putExtra("eventCode", iEventCode);
                startActivityForResult(it, requestCode);
            }
        }
    };

    private void findViews() {
        m_btn_time = (Button) findViewById(R.id.activity_clock_home_btn_time);
    }

    private void setButtonListener() {
        m_btn_time.setOnClickListener(btnForTime);
    }
}
