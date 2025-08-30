package org.example.engine;

import org.example.community.Community;
import org.example.config.Config;
import org.example.model.Survey;

import java.util.Objects;

public class SurveyPreconditions {

    private static final String ERR_ACTIVE_SURVEY =
            "There is an active survey. Close the current survey before opening a new one.";
    private static final String ERR_COMMUNITY_NULL =
            "Community is null";
    private static final String ERR_STATE_NULL =
            "surveyState is null";
    private static final String ERR_COMMUNITY_TOO_SMALL_FMT =
            "At least %d community members are required to start a survey.";
    private static final String ERR_SURVEY_NULL =
            "Survey is null";
    private static final String ERR_QUESTIONS_EMPTY =
            "Survey has no questions";

    private Community community;
    private SurveyState surveyState;

    public SurveyPreconditions(Community community, SurveyState surveyState) {
        this.community = Objects.requireNonNull(community, ERR_COMMUNITY_NULL);
        this.surveyState = Objects.requireNonNull(surveyState, ERR_STATE_NULL);
    }

    public void validateBeforeStart(Survey survey) {
        requireNoActiveSurvey();
        requireCommunityLargeEnough();
        requireSurveyNotNull(survey);
        requireQuestionsNotEmpty(survey);
    }


    private void requireNoActiveSurvey() {
        if (this.surveyState.isSurveyOpen()) {
            throw new IllegalStateException(ERR_ACTIVE_SURVEY);
        }
    }

    private void requireCommunityLargeEnough() {
        int min = Config.getMinCommunityForSurvey();
        int size = this.community.size();
        if (size < min) {
            throw new IllegalStateException(String.format(ERR_COMMUNITY_TOO_SMALL_FMT, min));
        }
    }

    private static void requireSurveyNotNull(Survey survey) {
        if (survey == null) {
            throw new IllegalArgumentException(ERR_SURVEY_NULL);
        }
    }

    private static void requireQuestionsNotEmpty(Survey survey) {
        if (survey.getQuestions() == null || survey.getQuestions().isEmpty()) {
            throw new IllegalArgumentException(ERR_QUESTIONS_EMPTY);
        }
    }
}
