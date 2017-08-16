package com.example.chiayingwu.notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button m_btn_simple;
    private Button m_btn_bigText;

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
                case R.id.btn_bigText:
                    NotifyUtil.buildBigText(2, R.drawable.ic_launcher, "BigTextTitle", "The world is the planet Earth and all life upon it, including human civilization.[1] In a philosophical context, the world is the whole of the physical Universe, or an ontological world. In a theological context, the world is the material or the profane sphere, as opposed to the celestial, spiritual, transcendent or sacred. The \"end of the world\" refers to scenarios of the final end of human history, often in religious contexts.\n" +
                            "\n" +
                            "History of the world is commonly understood as spanning the major geopolitical developments of about five millennia, from the first civilizations to the present. In terms such as world religion, world language, world government, and world war, world suggests international or intercontinental scope without necessarily implying participation of the entire world.")
                            .show();
            }
        }
    };

    private void findViews() {
        m_btn_simple = (Button) findViewById(R.id.btn_simple);
        m_btn_bigText = (Button) findViewById(R.id.btn_bigText);
    }

    private void setButtonListener() {
        m_btn_simple.setOnClickListener(btnToNotify);
        m_btn_bigText.setOnClickListener(btnToNotify);
    }
}
