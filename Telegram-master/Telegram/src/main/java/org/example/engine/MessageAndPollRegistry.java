package org.example.engine;

import org.example.PollSendResult;
import org.example.SurveyState;

import java.util.HashMap;
import java.util.Map;

public class MessageAndPollRegistry {
    private SurveyState surveyState;
    private SurveyLocks locks;

    public MessageAndPollRegistry(SurveyState surveyState, SurveyLocks locks) {
        this.surveyState = surveyState;
        this.locks = locks;
    }

    public void remember(long chatId, int questionIndex, PollSendResult result) {
        synchronized (this.locks.getStateLock()) {
            if (result.getPollId() != null) {
                this.surveyState.getPollIdToQuestionIndex().put(result.getPollId(), questionIndex);
            }
            Map<Integer, Integer> perQuestion = this.surveyState.getMessageIdByChatIdAndQuestion().get(chatId);
            if (perQuestion == null) {
                perQuestion = new HashMap<Integer, Integer>();
                this.surveyState.getMessageIdByChatIdAndQuestion().put(chatId, perQuestion);
            }
            perQuestion.put(questionIndex, result.getMessageId());
        }
    }
}
