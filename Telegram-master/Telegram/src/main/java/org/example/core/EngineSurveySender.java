package org.example.core;

import org.example.engine.SurveyEngine;
import org.example.model.Survey;

import java.util.Objects;

public class EngineSurveySender implements SurveySender {
    private static final String LOG_PREFIX = "[EngineSurveySender] ";
    private static final String LOG_SEND = LOG_PREFIX + "sendSurvey";
    private static final String LOG_CLOSE = LOG_PREFIX + "closeSurvey";
    private static final String ERR_ENGINE_NULL = "SurveyEngine is null";
    private static final String ERR_SURVEY_NULL = "Survey is null";

    private SurveyEngine engine;

    public EngineSurveySender(SurveyEngine engine) {
        this.engine = Objects.requireNonNull(engine, ERR_ENGINE_NULL);
    }

    public void setEngine(SurveyEngine engine) {
        this.engine = Objects.requireNonNull(engine, ERR_ENGINE_NULL);
    }

    @Override
    public void sendSurvey(Survey survey) {
        Objects.requireNonNull(survey, ERR_SURVEY_NULL);
        log(LOG_SEND);
        engine.startSurvey(survey);
    }

    @Override
    public void closeSurvey(long surveyId) {
        log(LOG_CLOSE);
        engine.closeActiveSurvey();
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
