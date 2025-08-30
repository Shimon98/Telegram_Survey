package org.example.core;

import org.example.model.Survey;

public interface SurveySender {
    void sendSurvey(Survey survey) throws Exception;

    void closeSurvey(long surveyId) throws Exception;
}
