package com.example.chiayingwu.notification;

import android.content.Context;

/**
 * Created by chiaying.wu on 2017/8/30.
 */

public class Controller {
    private KeyValueDB m_keyValueDB = new KeyValueDB();

    public void initSharedPrefs(Context context){
        m_keyValueDB.getPrefs(context);
    }

    public String getEventData(Context context, int iEventId){
        return m_keyValueDB.getEventData(context, String.valueOf(iEventId));
    }
}
