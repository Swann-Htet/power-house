package com.powerhouse.zeroone;

import java.util.List;
import java.util.Map;

public class Match {
    private String matchId;
    private String team1;
    private String team2;
    private int team1VoteCount;
    private int team2VoteCount;
    private String urlTeam1;
    private String urlTeam2;
    private Map<String, Vote> votes; // Map to store user votes

    public Match() {
    }

    public Match(String matchId, String team1, String team2, int team1VoteCount, int team2VoteCount, Map<String, Vote> votes, String urlTeam1, String urlTeam2) {
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
        this.team1VoteCount = team1VoteCount;
        this.team2VoteCount = team2VoteCount;
        this.votes = votes;
        this.urlTeam1 = urlTeam1;
        this.urlTeam2 = urlTeam2;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getTeam1VoteCount() {
        return team1VoteCount;
    }

    public void setTeam1VoteCount(int team1VoteCount) {
        this.team1VoteCount = team1VoteCount;
    }

    public int getTeam2VoteCount() {
        return team2VoteCount;
    }

    public void setTeam2VoteCount(int team2VoteCount) {
        this.team2VoteCount = team2VoteCount;
    }

    public Map<String, Vote> getVotes() {
        return votes;
    }

    public void setVotes(Map<String, Vote> votes) {
        this.votes = votes;
    }

    public String getUrlTeam1() {
        return urlTeam1;
    }

    public void setUrlTeam1(String urlTeam1) {
        this.urlTeam1 = urlTeam1;
    }

    public String getUrlTeam2() {
        return urlTeam2;
    }

    public void setUrlTeam2(String urlTeam2) {
        this.urlTeam2 = urlTeam2;
    }
}
