package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by chiaying.wu on 2017/8/17.
 */

public class KeyValueDB {
    public static String PREF_NAME = "SP";
    public static String NO_DATA = "no_data";

    public KeyValueDB() {
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setEventData(Context context, String strKeyName, String strKeyValue) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(strKeyName, strKeyValue);
        editor.commit();
    }

    public static String getEventData(Context context, String strKeyName) {
        return getPrefs(context).getString(strKeyName, NO_DATA);
    }
}
