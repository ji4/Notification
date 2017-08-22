package com.example.chiayingwu.notification.builder;

import android.app.Notification;
import android.app.PendingIntent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.example.chiayingwu.notification.NotifyUtil;

import java.util.ArrayList;
import java.util.List;

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

    /*Button Action*/
    private List<BtnActionBean> listBtnActionBeans;

    public BaseBuilder addBtn(int icon, CharSequence text, PendingIntent pendingIntent) {
        if (listBtnActionBeans == null) {
            listBtnActionBeans = new ArrayList<>();
        }
        if (listBtnActionBeans.size() > 5) {
            throw new RuntimeException("5 buttons at most!");
        }
        listBtnActionBeans.add(new BtnActionBean(icon, text, pendingIntent));

        return this;
    }

    class BtnActionBean {
        private int icon;
        private CharSequence text;
        private PendingIntent pendingIntent;

        BtnActionBean(int icon, CharSequence text, PendingIntent pendingIntent) {
            this.icon = icon;
            this.text = text;
            this.pendingIntent = pendingIntent;
        }
    }
    /*End of Button Action*/

    private Uri soundUri;

    public BaseBuilder playSound() {
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // use system's built-in sound effect
        return this;
    }

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

    public BaseBuilder setSummaryText(CharSequence summaryText) {
        this.summaryText = summaryText;
        return this;
    }

    public BaseBuilder setContentIntent(PendingIntent contentIntent) {
        this.contentIntent = contentIntent;
        return this;
    }

    protected void setupNotificationBuilder() {
        builder = new Notification.Builder(NotifyUtil.g_context);
        builder.setContentIntent(contentIntent); //The Intent a notification activates

        if (smallIcon > 0)
            builder.setSmallIcon(smallIcon);

        //Buttons
        if (listBtnActionBeans != null && listBtnActionBeans.size() > 0) {
            for (BtnActionBean bean : listBtnActionBeans) {
                builder.addAction(bean.icon, bean.text, bean.pendingIntent);
            }
        }

        //sound
        if (soundUri != null) {
            builder.setSound(soundUri);
        }

        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
    }

    public void show() {
        setupNotificationBuilder();
        Notification notification = builder.build(); //build a notification
        NotifyUtil.notify(id, notification);
    }
}
