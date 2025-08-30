package org.example.gui;

import org.example.SurveyEngine;

import javax.swing.*;
import org.example.gui.cards.ProgressCard;
import org.example.gui.cards.ResultsCard;

public class SurveyWatcherThread extends Thread {
    private static String NAME = "SurveyWatcherThread";

    private SurveyEngine engine;
    private ProgressCard progress;
    private ResultsCard results;
    private AppFrame frame;

    public SurveyWatcherThread(SurveyEngine engine,
                               ProgressCard progress,
                               ResultsCard results,
                               AppFrame frame) {
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
                try { Thread.sleep(250); } catch (InterruptedException ie) { return; }
            }

            String header  = (engine == null ? "Survey ended." : engine.getLastHeader());
            String summary = (engine == null ? "" : engine.getLastSummary());

            // קודם מציגים תוצאות ותמונה ומעבירים לכרטיס תוצאות
            SwingUtilities.invokeLater(() -> {
                progress.setStatus(header == null ? "Survey ended." : header);
                results.showResults(summary == null ? "" : summary);
                results.showImage(engine == null ? null : engine.getLastChartPath());
                frame.showCard("RESULTS");

                // ואז (רשות) פותחים דיאלוג מידע — בלי Runnable
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
                            "Watcher error",
                            JOptionPane.ERROR_MESSAGE
                    )
            );
        }
    }
}
