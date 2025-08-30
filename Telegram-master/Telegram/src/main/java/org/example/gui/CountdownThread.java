package org.example.gui;

import org.example.gui.cards.ProgressCard;

public class CountdownThread extends ThreadFatherProject {
    private static final String NAME = "CountdownThread";
    private static final String MSG_SENDING_IN = "Sending in ";
    private static final String MSG_TIME_LEFT = "Time left ";

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

    @Override
    public void run() {
        while (seconds >= 0) {
            final String t = mmss(seconds);
            if (preSend) setDelayText(progress, MSG_SENDING_IN + t);
            else setTimeLeftText(progress, MSG_TIME_LEFT + t);

            if (seconds == 0) break;
            sleepMs(1000);
            seconds--;
        }
        if (preSend) clearDelay(progress);
        else clearTimeLeft(progress);
    }
}
