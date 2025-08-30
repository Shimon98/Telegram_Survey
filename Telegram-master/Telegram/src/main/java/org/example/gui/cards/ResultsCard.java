package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class ResultsCard extends JPanel {
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int PAD_ALL = 12;
    private static final int RESULTS_ROWS = 20;
    private static final int RESULTS_COLS = 60;
    private static final String LABEL_RESULTS_TEXT = "Results";
    private static final String DEFAULT_TEXT = "";
    private static final int IMG_SCROLL_W = 760;
    private static final int IMG_SCROLL_H = 260;

    private JTextArea area;
    private JLabel imageLabel;


    public ResultsCard() {
        super(new BorderLayout(H_GAP, V_GAP));
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        area = new JTextArea(RESULTS_ROWS, RESULTS_COLS);
        imageLabel = new JLabel();
        area.setEditable(false);
        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
    }

    private void layoutComponents() {
        add(new JLabel(LABEL_RESULTS_TEXT), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(new JScrollPane(area), BorderLayout.CENTER);

        JScrollPane imageScroll = new JScrollPane(imageLabel);
        imageScroll.setBorder(BorderFactory.createEmptyBorder());
        imageScroll.setPreferredSize(new Dimension(IMG_SCROLL_W, IMG_SCROLL_H));

        center.add(imageScroll, BorderLayout.SOUTH);
        add(center, BorderLayout.CENTER);
    }

    public void showResults(String text) {
        area.setText(text == null ? DEFAULT_TEXT : text);
        area.setCaretPosition(0);
        revalidate();
        repaint();
    }

    public void showImage(String path) {
        try {
            if (path == null) {
                imageLabel.setIcon(null);
                return;
            }
            imageLabel.setIcon(new ImageIcon(path));
        } catch (Exception e) {
            imageLabel.setIcon(null);
        }
    }
}
