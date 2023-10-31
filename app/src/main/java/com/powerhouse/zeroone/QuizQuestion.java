package com.powerhouse.zeroone;

public class QuizQuestion {
    private String quizIdRe;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String imageUrl;

    public QuizQuestion() {

    }

    public QuizQuestion(String quizIdRe, String questionText, String option1, String option2, String option3, String imageUrl) {
        this.quizIdRe = quizIdRe;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.imageUrl = imageUrl;
    }

    public String getQuizIdRe() {
        return quizIdRe;
    }

    public void setQuizIdRe(String quizIdRe) {
        this.quizIdRe = quizIdRe;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}

