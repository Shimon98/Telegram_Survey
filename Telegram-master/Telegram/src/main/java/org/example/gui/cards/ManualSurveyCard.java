package org.example.gui.cards;

import org.example.gui.SurveyBuiltListener;
import org.example.gui.components.QuestionEditorPanel;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.util.Validate;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManualSurveyCard extends JPanel {
    private static final int GAP = 8;
    private static final int GAP_SMALL = GAP - 2;
    private static final int PADDING = 12;
    private static final int GRID_ROWS = 0;
    private static final int GRID_COLS_SINGLE = 1;
    private static final int TOOLBAR_ALIGN = FlowLayout.LEFT;
    private static final int TOOLBAR_VGAP = 0;

    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 3;

    private static final String LABEL_SURVEY_TITLE = "Survey title:";
    private static final String BUTTON_ADD_TEXT = "Add question";
    private static final String BUTTON_REMOVE_TEXT = "Remove question";
    private static final String BUTTON_PREVIEW_TEXT = "Preview";
    private static final String ERR_DIALOG_TITLE = "Validation error";
    private static final String ERR_FIELD_TITLE = "Title";
    private static final String QUESTION_LABEL_FMT = "Question %d";
    private static final String PREVIEW_TITLE_PREFIX = "Title: ";
    private static final String QUESTION_PREFIX = "Q";
    private static final String OPTION_INDENT = "  ";
    private static final String OPTION_NUM_SUFFIX = ") ";
    private static final String NL = "\n";
    private static final String NL2 = "\n\n";
    private static final int DISPLAY_INDEX_OFFSET = 1;

    private SurveyBuiltListener listener;
    private JTextField titleField;
    private JPanel editorsPanel;
    private List<QuestionEditorPanel> editors;
    private JButton addBtn;
    private JButton removeBtn;
    private JButton previewBtn;

    public ManualSurveyCard(SurveyBuiltListener listener) {
        super(new BorderLayout(GAP, GAP));
        this.listener = listener;

        this.titleField = new JTextField();
        this.editorsPanel = new JPanel(new GridLayout(GRID_ROWS, GRID_COLS_SINGLE, GAP, GAP));
        this.editors = new ArrayList<>();
        this.addBtn = new JButton(BUTTON_ADD_TEXT);
        this.removeBtn = new JButton(BUTTON_REMOVE_TEXT);
        this.previewBtn = new JButton(BUTTON_PREVIEW_TEXT);

        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        JPanel top = new JPanel(new BorderLayout(GAP_SMALL, GAP_SMALL));
        top.add(new JLabel(LABEL_SURVEY_TITLE), BorderLayout.WEST);
        top.add(titleField, BorderLayout.CENTER);

        JPanel toolbar = new JPanel(new FlowLayout(TOOLBAR_ALIGN, GAP_SMALL, TOOLBAR_VGAP));
        toolbar.add(addBtn);
        toolbar.add(removeBtn);
        toolbar.add(previewBtn);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(editorsPanel), BorderLayout.CENTER);
        add(toolbar, BorderLayout.SOUTH);

        addQuestion();

        addBtn.addActionListener(e -> {
            addQuestion();
        });
        removeBtn.addActionListener(e -> {
            removeQuestion();
        });
        previewBtn.addActionListener(e -> {
            buildSurvey();
        });

        syncButtons();
    }

    private void addQuestion() {
        if (editors.size() >= MAX_QUESTIONS) {
            return;
        }
        QuestionEditorPanel panel = new QuestionEditorPanel(editors.size());
        editors.add(panel);
        editorsPanel.add(panel);
        refreshEditors();
        syncButtons();
    }

    private void removeQuestion() {
        if (editors.size() <= MIN_QUESTIONS) {
            return;
        }
        int lastIndex = editors.size() - 1;
        QuestionEditorPanel removed = editors.remove(lastIndex);
        editorsPanel.remove(removed);
        refreshEditors();
        syncButtons();
    }

    private void syncButtons() {
        boolean canAdd = editors.size() < MAX_QUESTIONS;
        boolean canRemove = editors.size() > MIN_QUESTIONS;
        addBtn.setEnabled(canAdd);
        removeBtn.setEnabled(canRemove);
    }

    private void refreshEditors() {
        editorsPanel.revalidate();
        editorsPanel.repaint();
    }

    private void buildSurvey() {
        try {
            String title = Validate.requireText(titleField.getText(), ERR_FIELD_TITLE);

            List<Question> questions = new ArrayList<>();
            for (int i = 0; i < editors.size(); i++) {
                QuestionEditorPanel ed = editors.get(i);
                String qText = Validate.requireText(ed.getQuestionText(),
                        String.format(QUESTION_LABEL_FMT, i + DISPLAY_INDEX_OFFSET));
                List<String> opts = ed.getOptionTexts();
                questions.add(Question.create(i, qText, opts));
            }

            Survey survey = Survey.create(System.currentTimeMillis(), title, questions);
            listener.onSurveyBuilt(survey);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), ERR_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private String buildPreviewText(Survey s) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREVIEW_TITLE_PREFIX).append(s.getTitle()).append(NL2);

        for (Question q : s.getQuestions()) {
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
        return sb.toString();
    }
}
