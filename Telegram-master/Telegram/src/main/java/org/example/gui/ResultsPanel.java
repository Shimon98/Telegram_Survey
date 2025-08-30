package org.example.gui;

import org.example.core.ResultsUtils;
import org.example.core.SurveyResult;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResultsPanel extends JPanel {
    private SurveyResult current;

    public ResultsPanel() {
        super(new BorderLayout(10, 10));
    }

    public void setData(SurveyResult result) {
        this.current = result;
        removeAll();
        add(buildContent(result), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JComponent buildContent(SurveyResult result) {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Results — " + result.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        wrap.add(title);
        wrap.add(Box.createVerticalStrut(8));

        for (SurveyResult.QuestionResult qRes : result.getQuestions()) {
            wrap.add(renderQuestion(qRes, result.getTotalVoters()));
            wrap.add(Box.createVerticalStrut(12));
        }
        return new JScrollPane(wrap);
    }

    private JComponent renderQuestion(SurveyResult.QuestionResult qRes, int totalVoters) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel q = new JLabel(qRes.getQuestionText());
        q.setFont(q.getFont().deriveFont(Font.BOLD));
        p.add(q);
        p.add(Box.createVerticalStrut(4));

        List<SurveyResult.OptionResult> sorted = ResultsUtils.sortByVotesDesc(qRes.getOptions());
        for (SurveyResult.OptionResult opt : sorted) {
            int votes = opt.getVotes();
            String line = opt.getText() + " — " + votes + " (" + ResultsUtils.percent(votes, totalVoters) + ")";
            p.add(new JLabel(line));
        }
        return p;
    }
}
