//package com.example.chiayingwu.notification;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//public class MainActivity extends AppCompatActivity {
//    private Button m_btn_simple, m_btn_bigText, m_btn_inbox, m_btn_bigPic, m_btn_progress;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        findViews();
//        setButtonListener();
//        NotifyUtil.init(getApplicationContext());
//    }
//
//    Button.OnClickListener btnToNotify = new Button.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.btn_simple:
//                    NotifyUtil.build(NotifyUtil.BUILD_SIMPLE,1, R.drawable.ic_launcher, "I'm title", "I'm content").show();
//                    NotifyUtil.buildSimple(1, R.drawable.ic_launcher, "I'm title", "I'm content", null)
//                            .addBtn(R.mipmap.ic_launcher, "left", NotifyUtil.buildIntent(MainActivity.class))
//                            .addBtn(R.mipmap.ic_launcher, "right", NotifyUtil.buildIntent(MainActivity.class))
//                            .playSound()
//                            .show();
//                    break;
//                case R.id.btn_bigText:
//                    NotifyUtil.buildBigText(2, R.drawable.ic_launcher, "BigTextTitle", "The world is the planet Earth and all life upon it, including human civilization.[1] In a philosophical context, the world is the whole of the physical Universe, or an ontological world. In a theological context, the world is the material or the profane sphere, as opposed to the celestial, spiritual, transcendent or sacred. The \"end of the world\" refers to scenarios of the final end of human history, often in religious contexts.\n" +
//                            "\n" +
//                                "History of the world is commonly understood as spanning the major geopolitical developments of about five millennia, from the first civilizations to the present. In terms such as world religion, world language, world government, and world war, world suggests international or intercontinental scope without necessarily implying participation of the entire world.")
//                            .show();
//                    break;
//                case R.id.btn_inbox:
//                    NotifyUtil.buildInbox(3, R.drawable.ic_launcher, "inbox title")
//                            .addMsg("1. someone published an article.")
//                            .addMsg("2. It's sunny today.")
//                            .show();
//                    break;
//                case R.id.btn_bigPic:
//                    NotifyUtil.buildBigPic(4, R.drawable.ic_launcher, "picTitle", "allen uploaded a picture!", "summary")
//                            .setPicture(R.drawable.scenery)
//                            .show();
//                    break;
//                case R.id.btn_progress:
//                    NotifyUtil.buildProcess(5, R.drawable.ic_launcher, "Downloading", 5, 100).show();
//                    break;
//            }
//        }
//    };
//
//    private void findViews() {
//        m_btn_simple = (Button) findViewById(R.id.btn_simple);
//        m_btn_bigText = (Button) findViewById(R.id.btn_bigText);
//        m_btn_inbox = (Button) findViewById(R.id.btn_inbox);
//        m_btn_bigPic = (Button) findViewById(R.id.btn_bigPic);
//        m_btn_progress = (Button) findViewById(R.id.btn_progress);
//    }
//
//    private void setButtonListener() {
//        m_btn_simple.setOnClickListener(btnToNotify);
//        m_btn_bigText.setOnClickListener(btnToNotify);
//        m_btn_inbox.setOnClickListener(btnToNotify);
//        m_btn_bigPic.setOnClickListener(btnToNotify);
//        m_btn_progress.setOnClickListener(btnToNotify);
//    }
//}
