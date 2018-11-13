package com.example.asus.pillreminder;

/**
 * Created by asus on 6/5/2016.
 */
public class PillData {

    private String pillName;
    private String syptom;
    private String amount;
    private String takeTime;
    private String pnumber;

    public PillData() {
    }

    public PillData(String pillName, String syptom, String amount, String takeTime, String number) {
        this.pillName = pillName;
        this.syptom = syptom;
        this.amount = amount;
        this.takeTime = takeTime;
        this.pnumber = number;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getSyptom() {
        return syptom;
    }

    public void setSyptom(String syptom) {
        this.syptom = syptom;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTakeTime() {
        return takeTime;
    }

    public String[] getAllTime(){
        return takeTime.split(" ");
    }

    public void setTakeTime(String takeTime) {
        this.takeTime = takeTime;
    }

    public String getNumber() {
        return pnumber;
    }

    public void setNumber(String number) {
        this.pnumber = number;
    }
}
