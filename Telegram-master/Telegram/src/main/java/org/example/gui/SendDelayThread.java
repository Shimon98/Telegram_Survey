package org.example.gui;

import javax.swing.*;

/** Sleeps off the EDT, then triggers AppFrame.startSendingNow() on the EDT. */
public class SendDelayThread extends Thread {
    private static String NAME = "SendDelayThread";
    private AppFrame frame;
    private int delayMinutes;

    public SendDelayThread(AppFrame frame, int delayMinutes) {
        super(NAME);
        this.frame = frame;
        this.delayMinutes = Math.max(0, delayMinutes);
        setDaemon(true);
    }

    @Override
    public void run() {
        try { Thread.sleep(delayMinutes * 60L * 1000L); } catch (InterruptedException ignored) {}
        SwingUtilities.invokeLater(new Runnable() { @Override public void run() { frame.startSendingNow(); } });
    }
}