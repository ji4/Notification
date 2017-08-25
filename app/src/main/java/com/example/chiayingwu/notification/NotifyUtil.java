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

import java.text.SimpleDateFormat;

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

    public static final int BUILD_SIMPLE = 0;
    public static final int BUILD_BIG_TEXT = 1;
    public static final int BUILD_INBOX = 2;
    public static final int BUILD_BIG_PIC = 3;
    public static final int BUILD_PROCESS = 4;
    public static final int BUILD_ACTION = 5;

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

    public static ProcessBuilder buildTimeProcess(int id, int smallIcon, CharSequence contentTitle, long startTime, long endTime) {
        ProcessBuilder builder = new ProcessBuilder();
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss");
        builder.setBase(smallIcon, contentTitle, timeFormatter.format(System.currentTimeMillis()) + "/" + timeFormatter.format(endTime)).setId(id); //text
        int max = (int) (endTime - startTime);
        int process = (int) (max - (endTime - System.currentTimeMillis()));
        builder.setProgress(max, process, false); //bar
        return builder;
    }

    public static void notify(int id, Notification notification) {
        m_notificationManager.notify(id, notification);
    }

    public static PendingIntent buildIntent(Class _class) {
        int iFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.g_context, _class); //get intent of current Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(NotifyUtil.g_context, 0, intent, iFlags); //get pending intent
        return pi;
    }

    public static PendingIntent buildService(Class _class, int iEventId) {
        int iFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.g_context, _class); //get intent of current Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_REMIND_LATER, iEventId);
        PendingIntent pi = PendingIntent.getService(g_context, 1, intent, iFlags); //get pending service
        return pi;
    }

    public static void cancel(int id) {
        if (m_notificationManager != null) {
            m_notificationManager.cancel(id);
        }
    }

    public static void cancelAll(){
        if(m_notificationManager!=null){
            m_notificationManager.cancelAll();
        }
    }


}
