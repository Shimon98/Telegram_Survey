package org.example.gui.cards;

import org.example.model.Question;
import org.example.model.Survey;

import javax.swing.*;
import java.awt.*;

public class PreviewCard extends JPanel {
    private static final int H_GAP = 8;
    private static final int V_GAP = 8;
    private static final int PAD_ALL = 12;
    private static final int PREVIEW_ROWS = 20;
    private static final int PREVIEW_COLS = 60;

    private static final String LABEL_PREVIEW_TEXT = "Preview";
    private static final String PREVIEW_TITLE_PREFIX = "Title: ";
    private static final String QUESTION_PREFIX = "Q";
    private static final String OPTION_INDENT = "  ";
    private static final String OPTION_NUM_SUFFIX = ") ";
    private static final String NL = "\n";
    private static final String NL2 = "\n\n";

    private static final int DISPLAY_INDEX_OFFSET = 1;

    private JTextArea area;
    private JSpinner delaySpinner;


    public PreviewCard() {
        super(new BorderLayout(H_GAP, V_GAP));
        this.delaySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 120, 1));

        this.area = new JTextArea(PREVIEW_ROWS, PREVIEW_COLS);
        area.setEditable(false);
        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Send after (minutes):"));
        top.add(delaySpinner);
        add(top, BorderLayout.NORTH);

        add(new JLabel(LABEL_PREVIEW_TEXT), BorderLayout.NORTH);
        add(new JScrollPane(area), BorderLayout.CENTER);

    }

    public void bindSurvey(Survey survey) {
        if (survey == null) {
            area.setText("");
            area.setCaretPosition(0);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(PREVIEW_TITLE_PREFIX).append(survey.getTitle()).append(NL2);

        for (Question q : survey.getQuestions()) {
            sb.append(QUESTION_PREFIX)
                    .append(q.getId() + DISPLAY_INDEX_OFFSET)
                    .append(": ")
                    .append(q.getText())
                    .append(NL);

            int i = 1;
            for (var opt : q.getOptions()) {
                sb.append(OPTION_INDENT)
                        .append(i++)
                        .append(OPTION_NUM_SUFFIX)
                        .append(opt.getText())
                        .append(NL);
            }

            sb.append(NL);
        }

        area.setText(sb.toString());
        area.setCaretPosition(0);
    }
    public int getDelayMinutesSafe() {
        try { return ((Integer) delaySpinner.getValue()).intValue(); } catch (Exception e) { return 0; }
    }
}
