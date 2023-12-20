package com.example.lottery.utils;

import android.util.Base64;

import com.example.lottery.entity.Lottery;

import java.util.ArrayList;
import java.util.List;

public class LotteryUtils {

    public static String win(String[] reds, String blue, String[] buyReds, String buyBlue) {
        boolean blueWin = false;
        if (buyBlue.equals(blue)) {
            blueWin = true;
        }

        int redWinCounts = 0;
        for (int i = 0; i < buyReds.length; i++) {
            for (int j = 0; j < reds.length; j++) {
                if (buyReds[i].equals(reds[j])) {
                    redWinCounts++;
                    break;
                }
            }
        }
        return judgeWin(redWinCounts, blueWin);
    }

    public static List<Lottery> getLotteryEntity(List<String> stringList) {
        ArrayList<Lottery> arrayList = new ArrayList<>();
        List<String> reds = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {

            if ((i + 1) % 7 == 0) {
                arrayList.add(Lottery.createLottery(reds.toArray(new String[6]), stringList.get(i)));
                reds.clear();
                continue;
            }
            reds.add(stringList.get(i));
        }
        return arrayList;
    }

    public static List<Lottery> getLotteryEntitySub(List<String> stringList) {

        ArrayList<Lottery> arrayList = new ArrayList<>();

        for (String item : stringList) {
            String[] reds = new String[6];
            reds[0] = item.substring(0, 2);
            reds[1] = item.substring(2, 4);
            reds[2] = item.substring(4, 6);
            reds[3] = item.substring(6, 8);
            reds[4] = item.substring(8, 10);
            reds[5] = item.substring(10, 12);
            arrayList.add(Lottery.createLottery(reds, item.substring(12, 14)));
        }
        return arrayList;
    }

    public static String judgeWin(int redWinCounts, boolean blueWin) {
        if (blueWin) {
            switch (redWinCounts) {
                case 6:
                    return "5000000";
                case 5:
                    return "3000";
                case 4:
                    return "200";
                case 3:
                    return "10";
                case 0:
                case 1:
                case 2:
                    return "5";
                default:
                    return "-2";

            }
        } else {
            switch (redWinCounts) {
                case 6:
                    return "100000";
                case 5:
                    return "200";
                case 4:
                    return "10";
                default:
                    return "-2";
            }
        }
    }

    public static String encodeToBase64(String originalString) {
        byte[] bytes = originalString.getBytes();
        byte[] encodedBytes = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encodedBytes);
    }

}
