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
    //    private Intent m_intent;
    private String m_strKeyName, m_strKeyValue;

    public void initSharedPrefs(Context context) {
        m_keyValueDB.getPrefs(context);
    }

    public String getEventData(Context context, int iEventId) {
        return m_keyValueDB.getEventData(context, String.valueOf(iEventId));
    }

    public void saveEventIdAndDataToBundle(int m_iEventId, String m_strEventData) {
        m_bundle = new Bundle();
        m_bundle.putInt(Constants.KEY_EVENT_ID, m_iEventId);
        m_bundle.putString(Constants.KEY_EVENT_DATA, m_strEventData);
    }

    public void saveEventActionToIntent(String strKeyName, String strKeyValue) {
        this.m_strKeyName = strKeyName;
        this.m_strKeyValue = strKeyValue;
    }

    public void startNotifyService(Context context) {
        Intent serviceIntent = new Intent(context, NotifyService.class);
        if (m_bundle != null) { //start by EventEditor
            serviceIntent.putExtras(m_bundle);
            m_bundle = null;
        } else if (m_strKeyName != null) { //start by EventManager
            serviceIntent.putExtra(m_strKeyName, m_strKeyValue);
            m_strKeyName = null;
        }
        context.startService(serviceIntent);
    }
}
