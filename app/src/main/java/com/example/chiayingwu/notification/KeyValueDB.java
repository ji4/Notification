package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/17.
 */

public class KeyValueDB {
    public static String PREF_NAME = "SP";
    private static String KEY_ID_LIST = "KEY_ID_LIST";

    public KeyValueDB() {
    }

    public SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private void commitToEditor(Context context, String strKeyName, String strKeyValue) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(strKeyName, strKeyValue);
        editor.commit();
    }

    public void setEventData(Context context, String strKeyName, String strKeyValue) {
        commitToEditor(context, strKeyName, strKeyValue);
    }

    public String getEventData(Context context, String strKeyName) {
        return getPrefs(context).getString(strKeyName, Constants.NO_DATA);
    }

    public void saveEventId(Context context, int iEventId) {
        String strEventId = String.valueOf(iEventId);
        String strEventIdList = getEventIdList(context);
        if (!strEventIdList.equals(Constants.NO_DATA)) {
            ArrayList<Integer> iArrEventId = DataConverter.convertToIntArray(strEventIdList);
            Boolean boolEventExist = checkEventExist(iEventId, iArrEventId);
            if (!boolEventExist) {
                iArrEventId.add(iEventId);
                strEventIdList = DataConverter.convertToString(iArrEventId);
                commitToEditor(context, KEY_ID_LIST, strEventIdList);
            }
        } else {
                commitToEditor(context, KEY_ID_LIST, strEventId);
        }
    }

    public void deleteEvent(Context context, int iEventId) {
        String strEventIdList = getEventIdList(context);
        if (!strEventIdList.equals(Constants.NO_DATA)) {
            ArrayList<Integer> iArrEventId = DataConverter.convertToIntArray(strEventIdList);
            iArrEventId.remove(Integer.valueOf(iEventId));
            strEventIdList = DataConverter.convertToString(iArrEventId);
            commitToEditor(context, KEY_ID_LIST, strEventIdList);
        }
    }

    public String getEventIdList(Context context) {
        return getPrefs(context).getString(KEY_ID_LIST, Constants.NO_DATA);
    }

    private Boolean checkEventExist(int iEventId, ArrayList<Integer> iArrEventId) {
        for (int i = 0; i < iArrEventId.size(); i++) {
            if (iEventId == iArrEventId.get(i)) { //if the event has existed
                return true;
            }
        }
        return false;
    }
}
