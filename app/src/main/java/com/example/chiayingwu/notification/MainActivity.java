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
        NotifyUtil.init(getApplicationContext());

        m_btn_simple = (Button) findViewById(R.id.btn_simple);
        m_btn_simple.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotifyUtil.buildSimple(1, R.drawable.ic_launcher, "I'm titile", "I'm content", null)
                        .show();
            }
        });
    }
}
