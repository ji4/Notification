package com.example.chiayingwu.notification;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/17.
 */

public class KeyValueDB {
    public static String PREF_NAME = "SP";
    public static String NO_DATA = "NO_DATA";
    private static String KEY_ID_LIST = "ID_LIST";
    private static final int EVENT_ADD = 0;
    private static final int EVENT_DELETE = 1;
    private static int eventOperation;

    public KeyValueDB() {
    }

    public static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    private static void commitToEditor(Context context, String strKeyName, String strKeyValue) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString(strKeyName, strKeyValue);
        editor.commit();
    }

    public static void setEventData(Context context, String strKeyName, String strKeyValue) {
        commitToEditor(context, strKeyName, strKeyValue);
    }

    public static String getEventData(Context context, String strKeyName) {
        return getPrefs(context).getString(strKeyName, NO_DATA);
    }

    public static void saveEventId(Context context, int iEventId) {
        eventOperation = EVENT_ADD;
        operateEventList(context, iEventId);
    }

    public static void deleteExpiredEvent(Context context, int iEventId) {
        eventOperation = EVENT_DELETE;
        operateEventList(context, iEventId);
    }

    public static String getEventIdList(Context context) {
        return getPrefs(context).getString(KEY_ID_LIST, NO_DATA);
    }

    private static void operateEventList(Context context, int iEventId) {
        String strEventId = String.valueOf(iEventId);
        String strEventIdList = getEventIdList(context);
        if (!strEventIdList.equals(NO_DATA)) {
            ArrayList<Integer> iArrEventId = DataConverter.convertToIntArray(strEventIdList);
            Boolean boolEventExist = checkEventExist(iEventId, iArrEventId);
            if (!boolEventExist) {
                switch (eventOperation) {
                    case EVENT_ADD:
                        iArrEventId.add(iEventId);
                        break;
                    case EVENT_DELETE:
                        iArrEventId.remove(Integer.valueOf(iEventId));
                        break;
                    default:
                        break;
                }
                strEventIdList = DataConverter.convertToString(iArrEventId);
                commitToEditor(context, KEY_ID_LIST, strEventIdList);
            }
        } else {
            if (eventOperation == EVENT_ADD) {
                commitToEditor(context, KEY_ID_LIST, strEventId);
            }
        }
    }

    private static Boolean checkEventExist(int iEventId, ArrayList<Integer> iArrEventId) {
        for (int i = 0; i < iArrEventId.size(); i++) {
            if (iEventId == iArrEventId.get(i)) { //if the event has existed
                return true;
            }
        }
        return false;
    }
}
