package com.example.chiayingwu.notification.builder;

import android.app.Notification;

/**
 * Created by chiaying.wu on 2017/8/17.
 */

public class ProcessBuilder extends BaseBuilder {
    private int m_iMax;
    private int m_iProgress;
    private boolean m_isInterminate;

    public BaseBuilder setProgress(int iMax, int iProgress, boolean isInterminate) {
        this.m_iMax = iMax;
        this.m_iProgress = iProgress;
        this.m_isInterminate = isInterminate;
        return this;
    }

    @Override
    protected void setupNotificationBuilder() {
        super.setupNotificationBuilder();
        builder.setProgress(m_iMax, m_iProgress, m_isInterminate);
        builder.setDefaults(0);
        builder.setPriority(Notification.PRIORITY_LOW);
    }
}
