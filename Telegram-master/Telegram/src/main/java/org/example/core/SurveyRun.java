package org.example.core;

import org.example.model.Survey;
import org.example.util.Constants;

import javax.swing.SwingUtilities;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public final class SurveyRun {
    private final long startedAtMs = System.currentTimeMillis();

    private final int totalMembersSnapshot;
    private final AtomicInteger completedMembers = new AtomicInteger(0);
    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final Survey survey;
    private final ResultsAggregator aggregator;
    private final UiControllerBridge ui;

    public SurveyRun(Survey survey, int totalMembersSnapshot,
                     ResultsAggregator aggregator, UiControllerBridge ui) {
        this.survey = survey;
        this.totalMembersSnapshot = totalMembersSnapshot;
        this.aggregator = aggregator;
        this.ui = ui;
        startWatchdogThread();
    }

    public void onMemberVoted(long memberId, int questionId, int optionIndex) {
        aggregator.recordVote(questionId, optionIndex, memberId);

        boolean finishedAll = aggregator.hasMemberCompletedAll(memberId, survey);
        if (finishedAll) {
            completedMembers.incrementAndGet();
        }
        maybeClose("ALL_ANSWERED", completedMembers.get() >= totalMembersSnapshot);
    }

    private void startWatchdogThread() {
        Thread t = new Thread(() -> {
            while (!closed.get()) {
                long elapsed = System.currentTimeMillis() - startedAtMs;
                if (elapsed >= Constants.SURVEY_DURATION_MS) {
                    maybeClose("TIMEOUT", true);
                }
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            }
        }, "survey-watchdog");
        t.setDaemon(true);
        t.start();
    }

    private void maybeClose(String reason, boolean condition) {
        if (!condition) {
            return;
        }
        if (!closed.compareAndSet(false, true)) {
            return;
        }

        final SurveyResult result = aggregator.buildResult(survey);
        SwingUtilities.invokeLater(() -> {
            ui.setResults(result);
            ui.showResults();
            try { org.example.SurveyEngine.markClosed(); } catch (Throwable ignore) {}
        });
    }
}
