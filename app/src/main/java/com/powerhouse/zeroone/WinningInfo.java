package com.powerhouse.zeroone;
import java.io.Serializable;

public class WinningInfo implements Serializable {
    private String userName;
    private String teamName;
    private String read;
    private String message;
    private String userID;
    private String matchID;

    public WinningInfo() {
        // Default constructor required for Firebase serialization
    }

    public WinningInfo(String userName, String message, String teamName, String read, String userID, String matchID) {
        this.userName = userName;
        this.teamName = teamName;
        this.read = read;
        this.message = message;
        this.userID = userID;
        this.matchID = matchID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMatchID() {
        return matchID;
    }

    public void setMatchID(String matchID) {
        this.matchID = matchID;
    }
}
