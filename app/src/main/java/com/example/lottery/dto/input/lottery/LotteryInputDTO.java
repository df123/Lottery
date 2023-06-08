package com.example.lottery.dto.input.lottery;

import java.util.List;

public class LotteryInputDTO {
    private int state;
    private String message;
    private int pageCount;
    private int countNum;
    private int Tflag;
    private List<ResultInputDTO> result;
    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public int getPageCount() {
        return pageCount;
    }

    public void setCountNum(int countNum) {
        this.countNum = countNum;
    }
    public int getCountNum() {
        return countNum;
    }

    public void setTflag(int Tflag) {
        this.Tflag = Tflag;
    }
    public int getTflag() {
        return Tflag;
    }

    public void setResult(List<ResultInputDTO> result) {
        this.result = result;
    }
    public List<ResultInputDTO> getResult() {
        return result;
    }
}
