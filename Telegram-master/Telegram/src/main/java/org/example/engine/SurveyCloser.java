package org.example.engine;

import org.example.SurveyState;
import org.example.TelegramPollEngine;

import java.util.HashMap;
import java.util.Map;

public class SurveyCloser {

    private TelegramPollEngine telegramPollEngine;
    private SurveyState surveyState;
    private SurveyLocks locks;

    public SurveyCloser(TelegramPollEngine telegramPollEngine, SurveyState surveyState, SurveyLocks locks) {
        this.telegramPollEngine = telegramPollEngine;
        this.surveyState = surveyState;
        this.locks = locks;
    }

    public void closeNow() {
        Map<Long, Map<Integer, Integer>> snapshot;

        synchronized (this.locks.getStateLock()) {
            if (!this.surveyState.isSurveyOpen()) return;
            snapshot = new HashMap<Long, Map<Integer, Integer>>(this.surveyState.getMessageIdByChatIdAndQuestion());
            this.surveyState.setSurveyOpen(false);
        }

        for (Map.Entry<Long, Map<Integer, Integer>> entry : snapshot.entrySet()) {
            long chatId = entry.getKey();
            Map<Integer, Integer> perQuestion = entry.getValue();
            if (perQuestion == null) continue;
            for (Map.Entry<Integer, Integer> byQuestion : perQuestion.entrySet()) {
                int messageId = byQuestion.getValue();
                this.telegramPollEngine.stopPollByMessageId(chatId, messageId);
            }
        }

    }
}
