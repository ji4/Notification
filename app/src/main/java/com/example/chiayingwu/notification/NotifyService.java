package com.example.chiayingwu.notification;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class NotifyService extends IntentService {
    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String dataString = intent.getDataString();


    }
}