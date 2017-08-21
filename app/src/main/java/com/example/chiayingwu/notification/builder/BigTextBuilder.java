package com.example.chiayingwu.notification.builder;

import android.app.Notification;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class BigTextBuilder extends BaseBuilder {

    @Override
    protected void setupNotificationBuilder() {
        super.setupNotificationBuilder();

        Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
        bigTextStyle.setBigContentTitle(contentTitle).bigText(contentText).setSummaryText(summaryText);
        builder.setStyle(bigTextStyle);
    }


}
