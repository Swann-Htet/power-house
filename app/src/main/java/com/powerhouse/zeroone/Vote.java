package com.powerhouse.zeroone;

public class Vote {
    private String teamSelected;
    private String username;
    private String userID;

    public Vote(String teamSelected, String username, String userID) {
        this.teamSelected = teamSelected;
        this.username = username;
        this.userID = userID;
    }

    public String getTeamSelected() {
        return teamSelected;
    }

    public String getUsername() {
        return username;
    }

    public String getUserID() {
        return userID;
    }
}
