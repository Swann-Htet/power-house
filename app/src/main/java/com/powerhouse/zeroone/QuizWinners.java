package com.powerhouse.zeroone;

import java.io.Serializable;

public class QuizWinners implements Serializable{
    private String answer;
    private String message;
    private String quizID;
    private String read;
    private String userID;

    public QuizWinners() {

    }

    public QuizWinners(String answer, String message, String quizID, String read, String userID) {
        this.answer = answer;
        this.message = message;
        this.quizID = quizID;
        this.read = read;
        this.userID = userID;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
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
}

