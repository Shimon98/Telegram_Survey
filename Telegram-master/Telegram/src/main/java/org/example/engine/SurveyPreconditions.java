package org.example.engine;

import org.example.community.Community;
import org.example.model.Survey;
import org.example.SurveyState;
import org.example.config.Config;

public class SurveyPreconditions {
    private static final String ERROR_ACTIVE_SURVEY =
            "There is an active survey. Close the current survey before opening a new one.";
    private static final String ERROR_SURVEY_NULL = "Survey is null";
    private static final String ERROR_QUESTIONS_EMPTY = "Survey has no questions";

    private Community community;
    private SurveyState surveyState;

    public SurveyPreconditions(Community community, SurveyState surveyState) {
        this.community = community;
        this.surveyState = surveyState;
    }

    public void validateBeforeStart(Survey survey) {
        if (this.surveyState.isSurveyOpen()) throw new IllegalStateException(ERROR_ACTIVE_SURVEY);

        int min = Config.getMinCommunityForSurvey();
        if (this.community == null || this.community.size() < min) {
            throw new IllegalStateException("At least " + min + " community members are required to start a survey.");
        }
        if (survey == null) throw new IllegalArgumentException(ERROR_SURVEY_NULL);
        if (survey.getQuestions() == null || survey.getQuestions().isEmpty())
            throw new IllegalArgumentException(ERROR_QUESTIONS_EMPTY);
    }
}
