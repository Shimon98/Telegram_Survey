package org.example.gui;

import org.example.gui.cards.ProgressCard;

import javax.swing.*;

public class ThreadFatherProject extends Thread {
    protected ThreadFatherProject(String name) {
        super(name);
        setDaemon(true);
    }

    public void ui(Runnable r) {
        if (SwingUtilities.isEventDispatchThread()) r.run();
        else SwingUtilities.invokeLater(r);
    }

    public void sleepMs(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public String mmss(int seconds) {
        if (seconds < 0) seconds = 0;
        String mm = String.format("%02d", seconds / 60);
        String ss = String.format("%02d", seconds % 60);
        return mm + ":" + ss;
    }

    protected void setStatus(ProgressCard progress, String text) {
        if (progress == null) return;
        ui(() -> progress.setStatus(text));
    }

    public void setDelayText(ProgressCard progress, String text) {
        if (progress == null) return;
        ui(() -> progress.setSendDelayCountdown(text));
    }

    public void setTimeLeftText(ProgressCard progress, String text) {
        if (progress == null) return;
        ui(() -> progress.setTimeLeft(text));
    }

    public void clearDelay(ProgressCard progress) {
        setDelayText(progress, "");
    }

    protected void clearTimeLeft(ProgressCard progress) {
        setTimeLeftText(progress, "");
    }

    public void info(JFrame frame, String msg, String title) {
        ui(() -> JOptionPane.showMessageDialog(frame, msg, title, JOptionPane.INFORMATION_MESSAGE));
    }

    public void error(JFrame frame, String msg, String title) {
        ui(() -> JOptionPane.showMessageDialog(frame, msg, title, JOptionPane.ERROR_MESSAGE));
    }
}
