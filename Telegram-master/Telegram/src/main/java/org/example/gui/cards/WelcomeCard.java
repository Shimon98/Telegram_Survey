package org.example.gui.cards;

import javax.swing.*;
import java.awt.*;

public class WelcomeCard extends JPanel {
    private static final int H_GAP = 0;
    private static final int V_GAP = 0;
    private static final int PAD_ALL = 0;
    private static final float FONT_SIZE_PT = 18f;
    private static final String WELCOME_TEXT = "Welcome! Build a survey using the toolbar.";
    private static final int LABEL_ALIGN = SwingConstants.CENTER;

    private JLabel label;

    public WelcomeCard() {
        super(new BorderLayout(H_GAP, V_GAP));

        this.label = new JLabel(WELCOME_TEXT, LABEL_ALIGN);
        label.setFont(label.getFont().deriveFont(FONT_SIZE_PT));

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        add(label, BorderLayout.CENTER);
    }
}
