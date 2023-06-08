package com.example.lottery.entity;

import java.util.ArrayList;
import java.util.List;

public class Lottery {
    public List<String> reds;
    public List<String> blues;

    public static Lottery createLottery(String[] reds, String blue) {
        Lottery lottery = new Lottery();

        ArrayList<String> arrayReds = new ArrayList<String>(reds.length);
        for (String item : reds) {
            arrayReds.add(item);
        }
        lottery.setReds(arrayReds);

        ArrayList<String> arrayBlues = new ArrayList<String>(1);
        arrayBlues.add(blue);
        lottery.setBlues(arrayBlues);

        return lottery;
    }

    public List<String> getReds() {
        return reds;
    }

    public void setReds(List<String> reds) {
        this.reds = reds;
    }

    public List<String> getBlues() {
        return blues;
    }

    public void setBlues(List<String> blues) {
        this.blues = blues;
    }
}
