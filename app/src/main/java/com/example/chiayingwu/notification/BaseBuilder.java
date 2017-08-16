package com.example.chiayingwu.notification;

import android.app.Notification;
import android.app.PendingIntent;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class BaseBuilder {
    //basic UI
    protected int smallIcon;
    protected CharSequence contentTitle;
    protected CharSequence contentText;

    protected CharSequence summaryText;

    //basic notification manager
    private int id;
    private PendingIntent contentIntent;

    protected Notification.Builder builder;


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

    public BaseBuilder setSummaryText(CharSequence summaryText){
        this.summaryText = summaryText;
        return this;
    }

    public BaseBuilder setContentIntent(PendingIntent contentIntent) {
        this.contentIntent = contentIntent;
        return this;
    }

    protected void setNotificationBuilder() {
        builder = new Notification.Builder(NotifyUtil.g_context);
        builder.setContentIntent(contentIntent); //The Intent a notification activates

        if (smallIcon > 0)
            builder.setSmallIcon(smallIcon);

        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
    }

    public void show() {
        setNotificationBuilder();
        Notification notification = builder.build(); //build a notification
        NotifyUtil.notify(id, notification);
    }
}
