package org.example.core;

import java.util.List;

public class SurveyResult {
    public static final class OptionResult {
        private final String text;
        private final int votes;

        public OptionResult(String text, int votes) {
            this.text = text;
            this.votes = votes;
        }

        public String getText() {
            return text;
        }

        public int getVotes() {
            return votes;
        }
    }

    public static final class QuestionResult {
        private final String questionText;
        private final List<OptionResult> options;

        public QuestionResult(String questionText, List<OptionResult> options) {
            this.questionText = questionText;
            this.options = options;
        }

        public String getQuestionText() {
            return questionText;
        }

        public List<OptionResult> getOptions() {
            return options;
        }
    }

    private final String title;
    private final List<QuestionResult> questions;
    private final int totalVoters;

    public SurveyResult(String title, List<QuestionResult> questions, int totalVoters) {
        this.title = title;
        this.questions = questions;
        this.totalVoters = totalVoters;
    }

    public String getTitle() {
        return title;
    }

    public List<QuestionResult> getQuestions() {
        return questions;
    }

    public int getTotalVoters() {
        return totalVoters;
    }
}
