package com.example.chiayingwu.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class NotifyUtil {
    public static Context g_context;

    private static NotificationManager m_notificationManager;

    public static void init(Context context) {
        g_context = context;
        m_notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
    }


    public static SingleLineBuilder buildSimple(int id, int smallIcon, CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
        SingleLineBuilder builder = new SingleLineBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id)
                .setContentIntent(contentIntent);
        return builder;
    }

    public static BigTextBuilder buildBigText(int id,int smallIcon,CharSequence contentTitle,CharSequence contentText){
        BigTextBuilder builder = new BigTextBuilder();
        builder.setBase(smallIcon, contentTitle, contentText).setId(id);
        return builder;
    }

    public static void notify(int iNotifyId, Notification notification) {
        m_notificationManager.notify(iNotifyId, notification);
    }


}
