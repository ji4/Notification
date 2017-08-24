package com.example.chiayingwu.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.chiayingwu.notification.builder.BigPicBuilder;
import com.example.chiayingwu.notification.builder.BigTextBuilder;
import com.example.chiayingwu.notification.builder.InboxBuilder;
import com.example.chiayingwu.notification.builder.ProcessBuilder;
import com.example.chiayingwu.notification.builder.SingleLineBuilder;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class NotifyUtil {
    public static Context g_context;

    public static final int BUILD_SIMPLE = 0;
    public static final int BUILD_BIG_TEXT = 1;
    public static final int BUILD_INBOX = 2;
    public static final int BUILD_BIG_PIC = 3;
    public static final int BUILD_PROCESS = 4;


    private static NotificationManager m_notificationManager;

    public static void init(Context context) {
        g_context = context;
        m_notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
    }

//    public static BaseBuilder build(int iBuildType, int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
//        BaseBuilder builder = new BaseBuilder();
//        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
//        return builder;
//    }


    public static SingleLineBuilder buildSimple(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id)
                .setContentIntent(contentIntent);
        return builder;
    }

    public static BigTextBuilder buildBigText(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        BigTextBuilder builder = new BigTextBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        return builder;
    }

    public static InboxBuilder buildInbox(int id, int smallIcon, CharSequence contentTitle) {
        InboxBuilder builder = new InboxBuilder();
        builder.setBase(smallIcon, contentTitle, "").setId(id);
        return builder;
    }

    public static BigPicBuilder buildBigPic(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, CharSequence summaryText) {
        BigPicBuilder builder = new BigPicBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        builder.setSummaryText(summaryText);
        return builder;
    }

    public static ProcessBuilder buildProcess(int id, int smallIcon, CharSequence contentTitle, int progress, int max) {
        ProcessBuilder builder = new ProcessBuilder();
        builder.setBase(smallIcon, contentTitle, progress + "/" + max).setId(id);
        builder.setProgress(max, progress, false);
        return builder;
    }

    public static void notify(int iNotifyId, Notification notification) {
        m_notificationManager.notify(iNotifyId, notification);
    }

    public static PendingIntent buildIntent(Class _class) {
        int iFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.g_context, _class); //get intent of current Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(NotifyUtil.g_context, 0, intent, iFlags); //get pending intent
        return pi;
    }


}
