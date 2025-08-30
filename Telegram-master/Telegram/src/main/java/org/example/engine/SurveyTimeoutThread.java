package org.example.engine;

import org.example.SurveyFinisher;
import org.example.SurveyState;

public class SurveyTimeoutThread extends Thread {
    private static final String NAME = "[SurveyTimeoutThread] ";

    private SurveyState surveyState;
    private SurveyFinisher surveyFinisher;
    private long timeoutMillis;

    public SurveyTimeoutThread(SurveyState surveyState, SurveyFinisher surveyFinisher, long timeoutMillis) {
        super(NAME);
        this.surveyState = surveyState;
        this.surveyFinisher = surveyFinisher;
        this.timeoutMillis = timeoutMillis;
    }
    @Override
    public void run() {
        System.out.println("[SurveyTimeoutThread] 5-minute timer started.");
        try {
            Thread.sleep(this.timeoutMillis);
        } catch (InterruptedException ignored) {}
        System.out.println("[SurveyTimeoutThread] 5 minutes passed.");
        if (this.surveyState.isSurveyOpen()) {
            this.surveyFinisher.finishTimeout();
        }
    }
}
