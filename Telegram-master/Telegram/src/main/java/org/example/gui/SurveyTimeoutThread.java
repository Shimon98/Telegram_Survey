package org.example.gui;

import org.example.engine.SurveyState;
import org.example.engine.SurveyFinisher;

public class SurveyTimeoutThread extends ThreadFatherProject {
    private static String NAME  = "[SurveyTimeoutThread]";
    private static String MSG_TIMER_STARTED   = "Timeout timer started.";
    private static String MSG_TIMEOUT_REACHED = "Timeout reached.";

    private SurveyState surveyState;
    private SurveyFinisher surveyFinisher;
    private long timeoutMillis;

    public SurveyTimeoutThread(SurveyState surveyState, SurveyFinisher surveyFinisher, long timeoutMillis) {
        super(NAME);
        this.surveyState = surveyState;
        this.surveyFinisher = surveyFinisher;
        this.timeoutMillis = timeoutMillis;
        setDaemon(true);
    }

    @Override
    public void run() {
        System.out.println(NAME + " " + MSG_TIMER_STARTED);

        long deadline = System.currentTimeMillis() + timeoutMillis;

        while (surveyState != null && surveyState.isSurveyOpen() && System.currentTimeMillis() < deadline) {
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            sleepMs(250);
        }

        System.out.println(NAME + " " + MSG_TIMEOUT_REACHED);
        if (surveyState != null && surveyState.isSurveyOpen()) {
            try {
                surveyFinisher.finishTimeout();
            } catch (Exception ignored) {

            }
        }
    }
}
