package com.example.asus.pillreminder;

/**
 * Created by asus on 6/7/2016.
 */
public class mainlistData {
    private String aName;
    private String aSpeak;
    private String aNumber;

    public mainlistData() {
    }

    public String getaNumber() {
        return aNumber;
    }

    public void setaNumber(String aNumber) {
        this.aNumber = aNumber;
    }

    public mainlistData(String aName, String aSpeak, String aNumber) {
        this.aName = aName;
        this.aSpeak = aSpeak;
        this.aNumber = aNumber;
    }

    public String getaName() {
        return aName;
    }

    public String getaSpeak() {
        return aSpeak;
    }


    public void setaName(String aName) {
        this.aName = aName;
    }

    public void setaSpeak(String aSpeak) {
        this.aSpeak = aSpeak;
    }


}
