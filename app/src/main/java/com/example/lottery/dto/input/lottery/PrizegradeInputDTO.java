package com.example.lottery.dto.input.lottery;

public class PrizegradeInputDTO {
    private int type;
    private String typenum;
    private String typemoney;
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setTypenum(String typenum) {
        this.typenum = typenum;
    }
    public String getTypenum() {
        return typenum;
    }

    public void setTypemoney(String typemoney) {
        this.typemoney = typemoney;
    }
    public String getTypemoney() {
        return typemoney;
    }
}
