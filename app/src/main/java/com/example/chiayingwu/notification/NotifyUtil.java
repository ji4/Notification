package com.example.chiayingwu.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class NotifyUtil {
    public static Context g_context;
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

    public BaseBuilder build(int iNotifyType, int id, int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        BaseBuilder builder = new BaseBuilder(iNotifyType);
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        return builder;
    }

    public PendingIntent buildIntent(Class _class) {
        int iFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.g_context, _class); //get intent of current Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pi = PendingIntent.getActivity(NotifyUtil.g_context, 0, intent, iFlags); //get pending intent
        return pi;
    }

    public PendingIntent buildService(Class _class, int iEventId) {
        int iFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        Intent intent = new Intent(NotifyUtil.g_context, _class); //get intent of current Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.KEY_REMIND_LATER, iEventId);
        PendingIntent pi = PendingIntent.getService(g_context, 1, intent, iFlags); //get pending service
        return pi;
    }

    public static void notify(int id, Notification notification) {
        m_notificationManager.notify(id, notification);
    }

    public static void cancel(int id) {
        if (m_notificationManager != null) {
            m_notificationManager.cancel(id);
        }
    }

    public static void cancelAll() {
        if (m_notificationManager != null) {
            m_notificationManager.cancelAll();
        }
    }


}
