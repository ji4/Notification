package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by chiaying.wu on 2017/8/30.
 */

public class Controller {
    private KeyValueDB m_keyValueDB = new KeyValueDB();
    private Bundle m_bundle;

    public void initSharedPrefs(Context context) {
        m_keyValueDB.getPrefs(context);
    }

    public String getEventData(Context context, int iEventId) {
        return m_keyValueDB.getEventData(context, String.valueOf(iEventId));
    }

    public void saveEventIdAndDataToBundle(String m_strEventData, int m_iEventId) {
        m_bundle = new Bundle();
        m_bundle.putString(Constants.KEY_EVENT_DATA, m_strEventData);
        m_bundle.putInt(Constants.KEY_EVENT_ID, m_iEventId);
    }

    public void startNotifyService(Context context) {
        Intent serviceIntent = new Intent(context, NotifyService.class);
        if (m_bundle != null) {
            serviceIntent.putExtras(m_bundle);
            m_bundle = null;
        }
        context.startService(serviceIntent);
    }
}
