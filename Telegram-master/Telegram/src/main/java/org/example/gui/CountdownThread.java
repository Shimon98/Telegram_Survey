package org.example.gui;

import org.example.gui.cards.ProgressCard;

import javax.swing.*;

public class CountdownThread extends Thread {
    private static final String NAME = "CountdownThread";

    private ProgressCard progress;
    private boolean preSend;
    private int seconds;

    public CountdownThread(ProgressCard progress, boolean preSend, int seconds) {
        super(NAME);
        this.progress = progress;
        this.preSend = preSend;
        this.seconds = Math.max(0, seconds);
        setDaemon(true);
    }

    @Override public void run() {
        while (seconds >= 0) {
            final int s = seconds;
            SwingUtilities.invokeLater(() -> {
                String mm = String.format("%02d", s / 60);
                String ss = String.format("%02d", s % 60);
                if (preSend) progress.setSendDelayCountdown("Sending in " + mm + ":" + ss);
                else         progress.setTimeLeft("Time left " + mm + ":" + ss);
            });
            if (seconds == 0) break;
            try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
            seconds--;
        }
        SwingUtilities.invokeLater(() -> {
            if (preSend) progress.setSendDelayCountdown("");
            else         progress.setTimeLeft("");
        });
    }
}
