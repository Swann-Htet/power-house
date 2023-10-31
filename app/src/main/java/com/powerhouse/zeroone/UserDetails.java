package com.powerhouse.zeroone;

public class UserDetails {
    private String mobileNumber;
    private String name;
    private String userID;
    private String rewardAccountKey;

    public UserDetails() {
        // Default constructor required for Firebase
    }

    public UserDetails(String mobileNumber, String name, String userID, String rewardAccountKey) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.userID = userID;
        this.rewardAccountKey = rewardAccountKey;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getName() {
        return name;
    }

    public String getUserID() {
        return userID;
    }

    public String getRewardAccountKey() {
        return rewardAccountKey;
    }

    public void setRewardAccountKey(String rewardAccountKey) {
        this.rewardAccountKey = rewardAccountKey;
    }
}





