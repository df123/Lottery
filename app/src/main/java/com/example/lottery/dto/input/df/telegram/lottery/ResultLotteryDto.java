package com.example.lottery.dto.input.df.telegram.lottery;

import java.util.Date;

public class ResultLotteryDto {
    private int indexNo;
    private String number;
    private String colorType;
    private int groupId;
    private String lastModificationTime;
    private String lastModifierId;
    private Date creationTime;
    private String creatorId;
    private int id;
    public void setIndexNo(int indexNo) {
        this.indexNo = indexNo;
    }
    public int getIndexNo() {
        return indexNo;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getNumber() {
        return number;
    }

    public void setColorType(String colorType) {
        this.colorType = colorType;
    }
    public String getColorType() {
        return colorType;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public int getGroupId() {
        return groupId;
    }

    public void setLastModificationTime(String lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }
    public String getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModifierId(String lastModifierId) {
        this.lastModifierId = lastModifierId;
    }
    public String getLastModifierId() {
        return lastModifierId;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
    public String getCreatorId() {
        return creatorId;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
