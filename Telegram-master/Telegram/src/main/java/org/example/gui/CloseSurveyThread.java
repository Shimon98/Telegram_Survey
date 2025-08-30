package org.example.gui;

import org.example.SurveyEngine;
import org.example.core.SurveySender;
import org.example.gui.cards.ProgressCard;
import org.example.gui.cards.ResultsCard;

import javax.swing.*;

public class CloseSurveyThread extends Thread {
    private static String NAME = "CloseSurveyThread";

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
            SwingUtilities.invokeLater(() -> progress.setStatus("Closing..."));
            sender.closeSurvey(surveyId);
            try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            String header = engine.getLastHeader();
            String summary = engine.getLastSummary();

            SwingUtilities.invokeLater(() -> {
                progress.setStatus(header == null ? "Survey closed." : header);
                results.showResults(summary == null ? "" : summary);
                results.showImage(engine == null ? null : engine.getLastChartPath());
                frame.showCard("RESULTS");

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(
                            frame,
                            header == null ? "Survey finished." : header,
                            "Survey finished",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                });
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(
                            frame,
                            ex.getMessage(),
                            "Close failed",
                            JOptionPane.ERROR_MESSAGE
                    )
            );
        }
    }
}
