package org.example.gui.cards;

import org.example.ai.AiClient;
import org.example.ai.AiSurveyParser;
import org.example.util.SurveyBuiltListener;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.util.Validate;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ChatGptSurveyCard extends JPanel {
    private static final int GAP_H = 8;
    private static final int GAP_V = 8;
    private static final int TOP_GAP_H = 6;
    private static final int TOP_GAP_V = 6;
    private static final int PAD_ALL = 12;
    private static final int PREVIEW_ROWS = 16;
    private static final int PREVIEW_COLS = 60;
    private static final String LABEL_TOPIC_TEXT = "Topic:";
    private static final String BUTTON_GEN_TEXT = "Generate from AI";
    private static final String DIALOG_ERR_TITLE = "Error";
    private static final String NL = "\n";
    private static final String NL2 = "\n\n";
    private static final String PROMPT_PREFIX = "Create a short multiple-choice survey about: ";
    private static final String TITLE_AI_PREFIX = "AI: ";
    private static final String PREVIEW_TITLE_PREFIX = "Title: ";
    private static final String QUESTION_PREFIX = "Q";
    private static final String OPTION_NUMBER_SUFFIX = ") ";
    private static final String OPTION_INDENT = "  ";
    private static final int DISPLAY_INDEX_OFFSET = 1;
    private static final String ERR_FIELD_TOPIC_LABEL = "Topic";

    private SurveyBuiltListener listener;
    private AiClient ai;
    private AiSurveyParser parser;
    private JTextField topicField;
    private JButton generateButton;
    private JTextArea previewArea;

    public ChatGptSurveyCard(SurveyBuiltListener listener, String id) {
        super(new BorderLayout(GAP_H, GAP_V));

        this.listener = listener;
        this.ai = new AiClient(id);
        this.parser = new AiSurveyParser();
        this.topicField = new JTextField();
        this.generateButton = new JButton(BUTTON_GEN_TEXT);
        this.previewArea = new JTextArea(PREVIEW_ROWS, PREVIEW_COLS);

        setBorder(BorderFactory.createEmptyBorder(PAD_ALL, PAD_ALL, PAD_ALL, PAD_ALL));
        previewArea.setEditable(false);
        JPanel top = new JPanel(new BorderLayout(TOP_GAP_H, TOP_GAP_V));
        top.add(new JLabel(LABEL_TOPIC_TEXT), BorderLayout.WEST);
        top.add(topicField, BorderLayout.CENTER);
        top.add(generateButton, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(previewArea), BorderLayout.CENTER);
        generateButton.addActionListener(e -> {
            generateFromAi();
        });
    }

    private void generateFromAi() {
        try {
            String topic = Validate.requireText(topicField.getText(), ERR_FIELD_TOPIC_LABEL);
            String prompt = buildPrompt(topic);

            String aiText = ai.messageGpt(prompt);
            List<Question> questions = parser.parseToQuestions(aiText);

            Survey survey = Survey.create(System.currentTimeMillis(), TITLE_AI_PREFIX + topic, questions);
            showPreview(survey);
            listener.onSurveyBuilt(survey);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), DIALOG_ERR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    private String buildPrompt(String topic) {
        return PROMPT_PREFIX + topic + NL2 + AiSurveyParser.getSystemFormatInstruction();
    }

    private void showPreview(Survey s) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREVIEW_TITLE_PREFIX).append(s.getTitle()).append(NL2);

        for (Question question : s.getQuestions()) {
            sb.append(QUESTION_PREFIX)
                    .append(question.getId() + DISPLAY_INDEX_OFFSET)
                    .append(": ")
                    .append(question.getText())
                    .append(NL);

            int i = 1;
            for (var opt : question.getOptions()) {
                sb.append(OPTION_INDENT)
                        .append(i++)
                        .append(OPTION_NUMBER_SUFFIX)
                        .append(opt.getText())
                        .append(NL);
            }
            sb.append(NL);
        }

        previewArea.setText(sb.toString());
        previewArea.setCaretPosition(0);
    }
}
