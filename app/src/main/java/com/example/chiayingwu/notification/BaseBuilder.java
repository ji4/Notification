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

    //basic info
    protected int m_iSmallIcon;
    protected CharSequence m_contentTitle;
    protected CharSequence m_contentText;

    protected CharSequence m_summaryText;

    //basic notification manager
    private int m_iId;
    private PendingIntent m_contentIntent;
    protected Notification.Builder m_builder;

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
    private Bitmap m_bitmap;
    @DrawableRes
    private int m_iBigPicture;

    public BaseBuilder setPicture(@DrawableRes int iDrawable) {
        if (m_iNotifyType == BUILD_BIG_PIC) {
            this.m_iBigPicture = iDrawable;
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
        this.m_iSmallIcon = smallIcon;
        this.m_contentTitle = contentTitle;
        this.m_contentText = contentText;
        return this;
    }

    public BaseBuilder setId(int id) {
        this.m_iId = id;
        return this;
    }

    public BaseBuilder setSummaryText(CharSequence summaryText) {
        this.m_summaryText = summaryText;
        return this;
    }

    public BaseBuilder setContentIntent(PendingIntent contentIntent) {
        this.m_contentIntent = contentIntent;
        return this;
    }

    protected void setupNotificationBuilder() {
        m_builder = new Notification.Builder(NotifyUtil.g_context);
        m_builder.setContentIntent(m_contentIntent); //The Intent a notification activates

        if (m_iSmallIcon > 0)
            m_builder.setSmallIcon(m_iSmallIcon);

        //Buttons
        if (listBtnActionBeans != null && listBtnActionBeans.size() > 0) {
            for (BtnActionBean bean : listBtnActionBeans) {
                m_builder.addAction(bean.icon, bean.text, bean.pendingIntent);
            }
        }

        //sound
        if (soundUri != null) {
            m_builder.setSound(soundUri);
        }

        m_builder.setContentTitle(m_contentTitle);
        m_builder.setContentText(m_contentText);

        switch (m_iNotifyType) {
            case BUILD_SIMPLE:
                break;
            case BUILD_BIG_TEXT:
                Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
                bigTextStyle.setBigContentTitle(m_contentTitle).bigText(m_contentText).setSummaryText(m_summaryText);
                m_builder.setStyle(bigTextStyle);
                break;
            case BUILD_INBOX:
                Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
                for (String msg : m_strArrMsgList) {
                    inboxStyle.addLine(msg);
                }
                String strSummaryText = "[" + m_strArrMsgList.size() + "] messages";
                inboxStyle.setSummaryText(strSummaryText);
                m_builder.setContentText("You have " + strSummaryText).setStyle(inboxStyle);
                break;
            case BUILD_BIG_PIC:
                Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
                if (m_bitmap == null || m_bitmap.isRecycled()) {
                    if (m_iBigPicture > 0) {
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inScaled = true;
                        options.inSampleSize = 2;
                        m_bitmap = BitmapFactory.decodeResource(NotifyUtil.g_context.getResources(), m_iBigPicture, options);
                    }
                }
                bigPictureStyle.bigPicture(m_bitmap).setBigContentTitle(m_contentTitle).setSummaryText(m_summaryText);
                m_builder.setStyle(bigPictureStyle);
                break;
            case BUILD_PROCESS:
                m_builder.setProgress(m_iMax, m_iProgress, m_isInterminate);
                m_builder.setDefaults(0);
                m_builder.setPriority(Notification.PRIORITY_LOW);
                break;
            case BUILD_ACTION:
                break;
            default:
                break;
        }
    }

    public void show() {
        setupNotificationBuilder();
        Notification notification = m_builder.build(); //build a notification
        NotifyUtil.notify(m_iId, notification);
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
