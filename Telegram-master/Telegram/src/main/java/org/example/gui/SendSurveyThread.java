package org.example.gui;

import org.example.core.SurveySender;
import org.example.gui.cards.ProgressCard;
import org.example.model.Survey;

public class SendSurveyThread extends ThreadFatherProject {
    private static final String NAME = "SendSurveyThread";
    private static final String MSG_SENDING = "Sending...";
    private static final String MSG_SENT_WAIT = "Sent. Waiting for answers...";
    private static final String TITLE_SEND_FAILED = "Send failed";

    private SurveySender sender;
    private Survey survey;
    private ProgressCard progress;

    public SendSurveyThread(SurveySender sender, Survey survey, ProgressCard progress) {
        super(NAME);
        this.sender = sender;
        this.survey = survey;
        this.progress = progress;
        setDaemon(true);
    }

    @Override
    public void run() {
        try {
            setStatus(progress, MSG_SENDING);
            sender.sendSurvey(survey);
            setStatus(progress, MSG_SENT_WAIT);
        } catch (Exception ex) {
            error(null, ex.getMessage(), TITLE_SEND_FAILED);
        }
    }
}
