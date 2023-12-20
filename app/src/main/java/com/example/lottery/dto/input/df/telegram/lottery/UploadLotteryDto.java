package com.example.lottery.dto.input.df.telegram.lottery;

public class UploadLotteryDto {
    private int IndexNo ;
    private String Number;
    private String ColorType;
    private int GroupId;

    public int getIndexNo() {
        return IndexNo;
    }

    public void setIndexNo(int indexNo) {
        IndexNo = indexNo;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getColorType() {
        return ColorType;
    }

    public void setColorType(String colorType) {
        ColorType = colorType;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int groupId) {
        GroupId = groupId;
    }
}
