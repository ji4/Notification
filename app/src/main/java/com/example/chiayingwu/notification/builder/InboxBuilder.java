package com.example.chiayingwu.notification.builder;

import android.app.Notification;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/17.
 */

public class InboxBuilder extends BaseBuilder {
    private ArrayList<String> m_strArrMsgList;

    public InboxBuilder addMsg(String msg) {
        if (m_strArrMsgList == null) {
            m_strArrMsgList = new ArrayList<>();
        }
        m_strArrMsgList.add(msg);
        return this;
    }

    @Override
    protected void setupNotificationBuilder() {
        super.setupNotificationBuilder();
        Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
        for (String msg : m_strArrMsgList) {
            inboxStyle.addLine(msg);
        }
        String strSummaryText = "[" + m_strArrMsgList.size() + "] messages";
        inboxStyle.setSummaryText(strSummaryText);
        builder.setContentText("You have " + strSummaryText).setStyle(inboxStyle);
    }
}
