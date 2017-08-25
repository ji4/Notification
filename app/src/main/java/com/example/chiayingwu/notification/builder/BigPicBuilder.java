package com.example.chiayingwu.notification.builder;

import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.DrawableRes;

import com.example.chiayingwu.notification.NotifyUtil;

/**
 * Created by chiaying.wu on 2017/8/16.
 */

public class BigPicBuilder extends BaseBuilder {
    private Bitmap bitmap;
    @DrawableRes private int iBigPicture;

    public BigPicBuilder setPicture(@DrawableRes int iDrawable) {
        this.iBigPicture = iDrawable;
        return this;
    }

    @Override
    protected void setupNotificationBuilder() {
        super.setupNotificationBuilder();
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
    }
}
