package org.example.gui;

import org.example.core.SurveySender;
import org.example.gui.cards.ProgressCard;
import org.example.model.Survey;

import javax.swing.*;

public class SendSurveyThread extends Thread {
    private static final String NAME = "SendSurveyThread";
    private  SurveySender sender;
    private  Survey survey;
    private ProgressCard progress;

    public SendSurveyThread(SurveySender sender, Survey survey, ProgressCard progress) {
        super(NAME);
        this.sender = sender;
        this.survey = survey;
        this.progress = progress;
        setDaemon(true);
    }

    @Override public void run() {
        try {
            SwingUtilities.invokeLater(() -> progress.setStatus("Sending..."));
            sender.sendSurvey(survey);
            SwingUtilities.invokeLater(() -> progress.setStatus("Sent. Waiting for answers..."));
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Send failed", JOptionPane.ERROR_MESSAGE));
        }
    }
}
