package org.example.gui;

import org.example.engine.SurveyEngine;
import org.example.core.SurveySender;
import org.example.gui.cards.ProgressCard;
import org.example.gui.cards.ResultsCard;

public class CloseSurveyThread extends ThreadFatherProject {
    private static String NAME = "CloseSurveyThread";
    private static String MSG_CLOSING = "Closing...";
    private static String MSG_SURVEY_CLOSED = "Survey closed.";
    private static String MSG_SURVEY_FINISHED = "Survey finished.";
    private static String TITLE_SURVEY_FINISHED = "Survey finished";
    private static String TITLE_CLOSE_FAILED = "Close failed";
    private static String CARD_RESULTS = "RESULTS";

    private SurveySender sender;
    private long surveyId;
    private SurveyEngine engine;
    private ProgressCard progress;
    private ResultsCard results;
    private AppFrame frame;

    public CloseSurveyThread(SurveySender sender,
                             long surveyId, SurveyEngine engine, ProgressCard progress,
                             ResultsCard results, AppFrame frame) {
        super(NAME);
        this.sender = sender;
        this.surveyId = surveyId;
        this.engine = engine;
        this.progress = progress;
        this.results = results;
        this.frame = frame;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            setStatus(progress, MSG_CLOSING);
            sender.closeSurvey(surveyId);
            sleepMs(150);

            final String header = (engine == null ? MSG_SURVEY_CLOSED : engine.getLastHeader());
            final String summary = (engine == null ? "" : engine.getLastSummary());
            final String img = (engine == null ? null : engine.getLastChartPath());

            ui(() -> {
                setStatus(progress, header == null ? MSG_SURVEY_CLOSED : header);
                results.showResults(summary == null ? "" : summary);
                results.showImage(img);
                frame.showCard(CARD_RESULTS);
                info(frame,
                        header == null ? MSG_SURVEY_FINISHED : header,
                        TITLE_SURVEY_FINISHED);
            });

        } catch (Exception ex) {
            error(frame, ex.getMessage(), TITLE_CLOSE_FAILED);
        }
    }
}
