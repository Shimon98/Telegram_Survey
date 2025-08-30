package org.example.engine;

import org.example.community.Community;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.PollSendResult;
import org.example.TelegramPollEngine;

import java.util.List;
import java.util.Set;

public class SurveySender {
    private static final String LOG_PREFIX = "[SurveySender] ";
    private static final String LOG_SENT = LOG_PREFIX + "Sent question #%d to chatId=%d (messageId=%d)";

    private Community community;
    private TelegramPollEngine telegramPollEngine;
    private OptionTextBuilder optionTextBuilder;
    private MessageAndPollRegistry registry;

    public SurveySender(Community community,
                        TelegramPollEngine telegramPollEngine,
                        OptionTextBuilder optionTextBuilder,
                        MessageAndPollRegistry registry) {
        this.community = community;
        this.telegramPollEngine = telegramPollEngine;
        this.optionTextBuilder = optionTextBuilder;
        this.registry = registry;
    }

    public void sendAllQuestionsToAllMembers(Survey survey) {
        Set<Long> allChatIds = this.community.getAllChetId();
        int questionsCount = survey.getQuestions().size();

        for (Long chatId : allChatIds) {
            int questionIndex = 0;
            while (questionIndex < questionsCount) {
                Question currentQuestion = survey.getQuestions().get(questionIndex);
                String questionText = currentQuestion.getText();
                List<String> optionTexts = this.optionTextBuilder.buildOptionTexts(currentQuestion.getOptions());
                try {
                    PollSendResult result = this.telegramPollEngine
                            .sendQuestionPoll(chatId, questionText, optionTexts);
                    this.registry.remember(chatId, questionIndex, result);
                    System.out.println(String.format(LOG_SENT, questionIndex, chatId, result.getMessageId()));
                } catch (Exception ignored) {
                }
                questionIndex = questionIndex + 1;
            }
        }
    }
}
