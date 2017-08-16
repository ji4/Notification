package com.example.chiayingwu.notification;

import android.app.Notification;
import android.app.PendingIntent;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class BaseBuilder {
    //basic UI
    private int smallIcon;
    private CharSequence contentTitle;
    private CharSequence contentText;

    //basic notification manager
    private int id;
    private PendingIntent contentIntent;

    protected Notification.Builder m_builder;


    public BaseBuilder setBase(int smallIcon, CharSequence contentTitle, CharSequence contentText) {
        this.smallIcon = smallIcon;
        this.contentTitle = contentTitle;
        this.contentText = contentText;
        return this;
    }

    public BaseBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public BaseBuilder setContentIntent(PendingIntent contentIntent) {
        this.contentIntent = contentIntent;
        return this;
    }

    private void setNotificationBuilder() {
        m_builder = new Notification.Builder(NotifyUtil.g_context);
        m_builder.setContentIntent(contentIntent); //The Intent a notification activates

        if (smallIcon > 0)
            m_builder.setSmallIcon(smallIcon);

        m_builder.setContentTitle(contentTitle);
        m_builder.setContentText(contentText);
    }

    public void show() {
        setNotificationBuilder();
        Notification notification = m_builder.build(); //build a notification
        NotifyUtil.notify(id, notification);
    }
}
