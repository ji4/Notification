package com.example.chiayingwu.notification;

/**
 * Created by chiaying.wu on 2017/8/25.
 */

public class Notify {
    public static void notify(int iBuildType, int iEventId) {
        switch (iBuildType) {
            case NotifyUtil.BUILD_SIMPLE:
                NotifyUtil.buildSimple(1, R.drawable.ic_launcher, "I'm title", "I'm content", null)
                        .playSound()
                        .show();
                break;
            case NotifyUtil.BUILD_BIG_TEXT:
                NotifyUtil.buildBigText(2, R.drawable.ic_launcher, "BigTextTitle", "The world is the planet Earth and all life upon it, including human civilization.[1] In a philosophical context, the world is the whole of the physical Universe, or an ontological world. In a theological context, the world is the material or the profane sphere, as opposed to the celestial, spiritual, transcendent or sacred. The \"end of the world\" refers to scenarios of the final end of human history, often in religious contexts.\n" +
                        "\n" +
                        "History of the world is commonly understood as spanning the major geopolitical developments of about five millennia, from the first civilizations to the present. In terms such as world religion, world language, world government, and world war, world suggests international or intercontinental scope without necessarily implying participation of the entire world.")
                        .show();
                break;
            case NotifyUtil.BUILD_INBOX:
                NotifyUtil.buildInbox(3, R.drawable.ic_launcher, "inbox title")
                        .addMsg("1. someone published an article.")
                        .addMsg("2. It's sunny today.")
                        .show();
                break;
            case NotifyUtil.BUILD_BIG_PIC:
                NotifyUtil.buildBigPic(4, R.drawable.ic_launcher, "picTitle", "allen uploaded a picture!", "summary")
                        .setPicture(R.drawable.scenery)
                        .show();
                break;
            case NotifyUtil.BUILD_PROCESS:
                NotifyUtil.buildProcess(5, R.drawable.ic_launcher, "Downloading", 5, 100).show();
                break;
            case NotifyUtil.BUILD_ACTION:
                NotifyUtil.buildSimple(6, R.drawable.ic_launcher, "I'm title", "I'm content", null)
                        .addBtn(R.mipmap.ic_launcher, "left", NotifyUtil.buildService(NotifyService.class, iEventId))
                        .addBtn(R.mipmap.ic_launcher, "right", NotifyUtil.buildIntent(MainActivity.class))
                        .show();
                break;
            default:
                break;
        }
    }
}
