package com.example.chiayingwu.notification;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/18.
 */

public class DataConverter {
    public static ArrayList<Integer> convertToIntArray(String strData) {
        ArrayList<Integer> iArrltData = new ArrayList<>();
        String[] strArrItemData = strData.split(",");
        for (String strEventItem : strArrItemData) {
            int iItemData = Integer.parseInt(strEventItem);
            iArrltData.add(iItemData);
        }
        return iArrltData;
    }

    public static String convertToString(ArrayList<Integer> iArrltData) {
        String strData = "";
        for (int iItemData : iArrltData) {
            strData += String.valueOf(iItemData);
        }
        return strData;
    }
}
