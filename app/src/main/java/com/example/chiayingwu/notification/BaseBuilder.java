package com.example.chiayingwu.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.DrawableRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.chiayingwu.notification.NotifyUtil.BUILD_ACTION;
import static com.example.chiayingwu.notification.NotifyUtil.BUILD_BIG_PIC;
import static com.example.chiayingwu.notification.NotifyUtil.BUILD_BIG_TEXT;
import static com.example.chiayingwu.notification.NotifyUtil.BUILD_INBOX;
import static com.example.chiayingwu.notification.NotifyUtil.BUILD_PROCESS;
import static com.example.chiayingwu.notification.NotifyUtil.BUILD_SIMPLE;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class BaseBuilder {
    private int m_iNotifyType;

    public BaseBuilder(int iBuildType) {
        this.m_iNotifyType = iBuildType;
    }

    //basic UI
    protected int smallIcon;
    protected CharSequence contentTitle;
    protected CharSequence contentText;

    protected CharSequence summaryText;

    //basic notification manager
    private int id;
    private PendingIntent contentIntent;
    protected Notification.Builder builder;

    /*inbox builder*/
    private ArrayList<String> m_strArrMsgList;

    public BaseBuilder addMsg(String msg) {
        if (m_strArrMsgList == null) {
            m_strArrMsgList = new ArrayList<>();
        }
        m_strArrMsgList.add(msg);
        return this;
    }

    /*big pic builder*/
    private Bitmap bitmap;
    @DrawableRes
    private int iBigPicture;

    public BaseBuilder setPicture(@DrawableRes int iDrawable) {
        if (m_iNotifyType == BUILD_BIG_PIC) {
            this.iBigPicture = iDrawable;
        }
        return this;
    }

    /*process builder*/
    private int m_iMax;
    private int m_iProgress;
    private boolean m_isInterminate;

    public BaseBuilder setProgress(int iMax, int iProgress, boolean isInterminate) {
        this.m_iMax = iMax;
        this.m_iProgress = iProgress;
        this.m_isInterminate = isInterminate;
        return this;
    }

    public BaseBuilder buildTimeProcess(int id, int smallIcon, CharSequence contentTitle, long startTime, long endTime) {
        if (m_iNotifyType == BUILD_PROCESS) {
            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss");
            this.setBase(smallIcon, contentTitle, timeFormatter.format(System.currentTimeMillis()) + "/" + timeFormatter.format(endTime)).setId(id); //text
            int max = (int) (endTime - startTime);
            int process = (int) (max - (endTime - System.currentTimeMillis()));
            this.setProgress(max, process, false); //bar
        }
        return this;
    }

    /*Button Action*/
    private List<BtnActionBean> listBtnActionBeans;

    public BaseBuilder addBtn(int icon, CharSequence text, PendingIntent pendingIntent) {
        if (m_iNotifyType == BUILD_ACTION) {
            if (listBtnActionBeans == null) {
                listBtnActionBeans = new ArrayList<>();
            }
            if (listBtnActionBeans.size() > 5) {
                throw new RuntimeException("5 buttons at most!");
            }
            listBtnActionBeans.add(new BtnActionBean(icon, text, pendingIntent));
        }
        return this;
    }

    /*Sound*/
    private Uri soundUri;

    public BaseBuilder playSound() {
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); // use system's built-in sound effect
        return this;
    }

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

        switch (m_iNotifyType) {
            case BUILD_SIMPLE:
                break;
            case BUILD_BIG_TEXT:
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.setBigContentTitle(contentTitle).bigText(contentText).setSummaryText(summaryText);
                builder.setStyle(bigTextStyle);
                break;
            case BUILD_INBOX:
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
                for (String msg : m_strArrMsgList) {
                    inboxStyle.addLine(msg);
                }
                String strSummaryText = "[" + m_strArrMsgList.size() + "] messages";
                inboxStyle.setSummaryText(strSummaryText);
                builder.setContentText("You have " + strSummaryText).setStyle(inboxStyle);
                break;
            case BUILD_BIG_PIC:
                Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
                if (bitmap == null || bitmap.isRecycled()) {
                    if (iBigPicture > 0) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = true;
                        options.inSampleSize = 2;
                        bitmap = BitmapFactory.decodeResource(NotifyUtil.g_context.getResources(), iBigPicture, options);
                    }
                }
                bigPictureStyle.bigPicture(bitmap).setBigContentTitle(contentTitle).setSummaryText(summaryText);
                builder.setStyle(bigPictureStyle);
                break;
            case BUILD_PROCESS:
                builder.setProgress(m_iMax, m_iProgress, m_isInterminate);
                builder.setDefaults(0);
                builder.setPriority(Notification.PRIORITY_LOW);
                break;
            case BUILD_ACTION:
                break;
            default:
                break;
        }
    }

    public void show() {
        setupNotificationBuilder();
        Notification notification = builder.build(); //build a notification
        NotifyUtil.notify(id, notification);
    }
}

class BtnActionBean {
    int icon;
    CharSequence text;
    PendingIntent pendingIntent;

    BtnActionBean(int icon, CharSequence text, PendingIntent pendingIntent) {
        this.icon = icon;
        this.text = text;
        this.pendingIntent = pendingIntent;
    }
}
