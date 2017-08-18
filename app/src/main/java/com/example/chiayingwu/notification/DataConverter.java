package com.example.chiayingwu.notification;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/18.
 */

public class DataConverter {
    public static ArrayList<Integer> convertEventDataToInt(String strEventData) {
        ArrayList<Integer> iArrltEventData = new ArrayList<>();
        String[] strArrEventItemData = strEventData.split(",");
        for (String strEventItem : strArrEventItemData) {
            int iEventItemData = Integer.parseInt(strEventItem);
            iArrltEventData.add(iEventItemData);
        }
        return iArrltEventData;
    }
}
