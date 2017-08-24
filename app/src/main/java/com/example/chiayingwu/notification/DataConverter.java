package com.example.chiayingwu.notification;

import java.util.ArrayList;

/**
 * Created by chiaying.wu on 2017/8/18.
 */

public class DataConverter {
    public static ArrayList<Integer> convertToIntArray(String strData) {
        ArrayList<Integer> iArrltData = new ArrayList<>();
        if(!strData.equals("")) {
            String[] strArrItemData = strData.split(",");
            for (String strEventItem : strArrItemData) {
                int iItemData = Integer.parseInt(strEventItem);
                iArrltData.add(iItemData);
            }
        }
        return iArrltData;
    }

    public static String convertToString(ArrayList<Integer> iArrltData) {
        String strData = "";
        if (iArrltData.size() > 0) {
            for (int iItemData : iArrltData) {
                strData += String.valueOf(iItemData) + ",";
            }
            strData = strData.substring(0, strData.length() - 1); //remove the last comma
        }
        return strData;
    }
}
