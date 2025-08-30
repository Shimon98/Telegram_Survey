package org.example.gui;

import org.example.engine.SurveyEngine;
import org.example.gui.cards.ProgressCard;
import org.example.gui.cards.ResultsCard;

public class SurveyWatcherThread extends ThreadFatherProject {
    private static final String NAME = "SurveyWatcherThread";
    private static final String MSG_SURVEY_ENDED = "Survey ended.";
    private static final String MSG_SURVEY_FINISHED = "Survey finished.";
    private static final String TITLE_SURVEY_FINISHED = "Survey finished";
    private static final String MSG_WATCHER_ERROR = "Watcher error";
    private static final String CARD_RESULTS = "RESULTS";

    private SurveyEngine engine;
    private ProgressCard progress;
    private ResultsCard results;
    private AppFrame frame;

    public SurveyWatcherThread(SurveyEngine engine, ProgressCard progress,
                               ResultsCard results, AppFrame frame) {
        super(NAME);
        this.engine = engine;
        this.progress = progress;
        this.results = results;
        this.frame = frame;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            while (engine != null && engine.isSurveyOpen()) {
                sleepMs(250);
                if (Thread.currentThread().isInterrupted()) return;
            }

            final String header = (engine == null ? MSG_SURVEY_ENDED : engine.getLastHeader());
            final String summary = (engine == null ? "" : engine.getLastSummary());
            final String imgPath = (engine == null ? null : engine.getLastChartPath());

            ui(() -> {
                setStatus(progress, header == null ? MSG_SURVEY_ENDED : header);
                results.showResults(summary == null ? "" : summary);
                results.showImage(imgPath);
                frame.showCard(CARD_RESULTS);

                info(frame,
                        header == null ? MSG_SURVEY_FINISHED : header,
                        TITLE_SURVEY_FINISHED);
            });

        } catch (Exception ex) {
            error(frame, ex.getMessage(), MSG_WATCHER_ERROR);
        }
    }
}
