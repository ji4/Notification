package com.example.chiayingwu.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button m_btn_simple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setButtonListener();
        NotifyUtil.init(getApplicationContext());
    }

    Button.OnClickListener btnToNotify = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_simple:
                    NotifyUtil.buildSimple(1, R.drawable.ic_launcher, "I'm titile", "I'm content", null)
                            .show();
                    break;
            }
        }
    };

    private void findViews() {
        m_btn_simple = (Button) findViewById(R.id.btn_simple);
    }

    private void setButtonListener() {
        m_btn_simple.setOnClickListener(btnToNotify);
    }
}
