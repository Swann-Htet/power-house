package com.powerhouse.zeroone;

public class ReadWriteDetails {
    public String doB, gender, mobile;

    public ReadWriteDetails(){}

    public String getDoB() {
        return doB;
    }

    public void setDoB(String doB) {
        this.doB = doB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public ReadWriteDetails(String textDob, String textGender, String textMobile){
        this.doB = textDob;
        this.gender = textGender;
        this.mobile = textMobile;
    }
}


