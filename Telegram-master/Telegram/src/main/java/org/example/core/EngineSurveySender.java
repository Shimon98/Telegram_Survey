package org.example.core;

import org.example.SurveyEngine;
import org.example.model.Survey;

public class EngineSurveySender implements SurveySender {
    private static final String LOG_PREFIX = "[EngineSurveySender] ";
    private SurveyEngine engine;

    public EngineSurveySender(SurveyEngine engine) {
        this.engine = engine;
    }

    @Override
    public void sendSurvey(Survey survey) {
        System.out.println(LOG_PREFIX + "sendSurvey");
        this.engine.startSurvey(survey);
    }

    @Override
    public void closeSurvey(long surveyId) {
        System.out.println(LOG_PREFIX + "closeSurvey");
        this.engine.closeActiveSurvey();
    }
}