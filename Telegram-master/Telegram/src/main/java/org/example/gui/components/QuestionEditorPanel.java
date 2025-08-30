package org.example.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.example.config.AppConst.*; // MIN_OPTIONS / MAX_OPTIONS

public final class QuestionEditorPanel extends JPanel {
    private static final int START_OPTION_FIELDS = MIN_OPTIONS; // start with MIN_OPTIONS fields
    private static final int OUTER_H_GAP = 8;
    private static final int OUTER_V_GAP = 8;
    private static final int INNER_GAP = 6;
    private static final int PAD_ALL = 8;
    private static final int GRID_ROWS = 0;
    private static final int GRID_COLS_SINGLE = 1;
    private static final int TOOLBAR_ALIGN = FlowLayout.LEFT;
    private static final int TOOLBAR_HGAP = INNER_GAP;
    private static final int TOOLBAR_VGAP = 0;
    private static final String LABEL_Q_PREFIX = "Q";
    private static final String LABEL_Q_SUFFIX = ":";
    private static final String LABEL_OPTIONS_FMT = "Options (%d..%d):";
    private static final String BUTTON_ADD_TEXT = "Add option";
    private static final String BUTTON_REMOVE_TEXT = "Remove option";
    private static final int DISPLAY_INDEX_OFFSET = 1;

    private int questionIndex;
    private JTextField questionField;
    private JPanel optionsPanel;
    private List<JTextField> optionFields;
    private JButton addOptBtn;
    private JButton remOptBtn;

    public QuestionEditorPanel(int index) {
        super(new BorderLayout(OUTER_H_GAP, OUTER_V_GAP));

        this.questionIndex = index;
        this.questionField = new JTextField();
        this.optionsPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS_SINGLE, INNER_GAP, INNER_GAP));
        this.optionFields = new ArrayList<>();
        this.addOptBtn = new JButton(BUTTON_ADD_TEXT);
        this.remOptBtn = new JButton(BUTTON_REMOVE_TEXT);

        JPanel top = new JPanel(new BorderLayout(INNER_GAP, INNER_GAP));
        top.add(new JLabel(buildQuestionLabel(index)), BorderLayout.WEST);
        top.add(questionField, BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(TOOLBAR_ALIGN, TOOLBAR_HGAP, TOOLBAR_VGAP));
        btns.add(addOptBtn);
        btns.add(remOptBtn);

        JPanel center = new JPanel(new BorderLayout(INNER_GAP, INNER_GAP));
        center.add(new JLabel(String.format(LABEL_OPTIONS_FMT, MIN_OPTIONS, MAX_OPTIONS)), BorderLayout.NORTH);
        center.add(optionsPanel, BorderLayout.CENTER);
        center.add(btns, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        for (int i = 0; i < START_OPTION_FIELDS; i++) {
            addOptionField();
        }
        updateButtons();

        addOptBtn.addActionListener(e -> {
            addOptionField();
            updateButtons();
        });
        remOptBtn.addActionListener(e -> {
            removeOptionField();
            updateButtons();
        });
    }

    private static String buildQuestionLabel(int index) {
        return LABEL_Q_PREFIX + (index + DISPLAY_INDEX_OFFSET) + LABEL_Q_SUFFIX;
    }

    private void addOptionField() {
        if (optionFields.size() >= MAX_OPTIONS) {
            return;
        }
        JTextField f = new JTextField();
        optionFields.add(f);
        optionsPanel.add(f);
        refresh();
    }

    private void removeOptionField() {
        if (optionFields.size() <= MIN_OPTIONS) {
            return;
        }
        int last = optionFields.size() - 1;
        JTextField f = optionFields.remove(last);
        optionsPanel.remove(f);
        refresh();
    }

    private void updateButtons() {
        boolean canAdd = optionFields.size() < MAX_OPTIONS;
        boolean canRemove = optionFields.size() > MIN_OPTIONS;
        addOptBtn.setEnabled(canAdd);
        remOptBtn.setEnabled(canRemove);
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    public String getQuestionText() {
        return questionField.getText().trim();
    }

    public List<String> getOptionTexts() {
        List<String> out = new ArrayList<>();
        for (JTextField f : optionFields) {
            String t = f.getText().trim();
            if (!t.isEmpty()) {
                out.add(t);
            }
        }
        return out;
    }

    public int getQuestionIndex() {
        return questionIndex;
    }
}
