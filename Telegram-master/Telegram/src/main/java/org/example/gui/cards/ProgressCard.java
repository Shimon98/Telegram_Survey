package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class ProgressCard extends JPanel {
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int PAD_ALL = 12;

    private static final String LABEL_PROGRESS_TEXT = "Progress";
    private static final String STATUS_READY_TEXT = "Ready";

    private JLabel status;
    private JLabel countdown;
    private JLabel preDelay;

    public ProgressCard() {
        super(new BorderLayout(H_GAP, V_GAP));
        this.status = new JLabel(STATUS_READY_TEXT);
        this.countdown = new JLabel("");
        this.preDelay = new JLabel("");

        JPanel center = new JPanel(new GridLayout(0,1,4,4));
        center.add(status);
        center.add(preDelay);
        center.add(countdown);

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        add(new JLabel(LABEL_PROGRESS_TEXT), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    public void setStatus(String text) {
        status.setText(text == null ? STATUS_READY_TEXT : text);
        revalidate(); repaint();
    }


    public void setSendDelayCountdown(String txt) {
        preDelay.setText(txt == null ? "" : txt);
        revalidate(); repaint();
    }


    public void setTimeLeft(String txt) {
        countdown.setText(txt == null ? "" : txt);
        revalidate(); repaint();
    }
}
