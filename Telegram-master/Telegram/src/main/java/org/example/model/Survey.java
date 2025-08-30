package org.example.model;

import org.example.ai.AiSurveyParser;
import org.example.config.AppConst;
import org.example.util.Validate;

import java.util.Collections;
import java.util.List;

public class Survey {
    private static final String ERR_TITLE_LABEL = "Survey title";
    private static final String ERR_QUESTIONS_LABEL = "Questions";

    private long id;
    private String title;
    private List<Question> questions;

    public Survey(long id, String title, List<Question> questions) {
        this.id = id;
        this.title = Validate.requireText(title, ERR_TITLE_LABEL);
        Validate.requireSizeBetween(questions, AppConst.MIN_QUESTIONS, AppConst.MAX_QUESTIONS, ERR_QUESTIONS_LABEL);
        this.questions = Collections.unmodifiableList(questions);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    @Override
    public String toString() {
        return "Survey{" + id + ", '" + title + "', q=" + questions.size() + "}";
    }

    
    public static Survey create(long id, String title, java.util.List<org.example.model.Question> questions) {
        return new Survey(id, title, questions);
    }
}
