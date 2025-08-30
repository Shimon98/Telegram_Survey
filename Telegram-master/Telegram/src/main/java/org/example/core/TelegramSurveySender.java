package org.example.core;

import org.example.community.Community;
import org.example.model.OptionForQuestion;
import org.example.model.Question;
import org.example.model.Survey;
import org.example.bot.TelegramGateway;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class TelegramSurveySender implements SurveySender {
    private static final String EMOJI_SURVEY = "📊 ";
    private static final String EMOJI_CLOSED = "✅ ";
    private static final boolean POLL_IS_ANONYMOUS = true;
    private static final String MSG_NEW_SURVEY_PREFIX = EMOJI_SURVEY + "New survey: ";
    private static final String MSG_CLOSED_FMT = EMOJI_CLOSED + "Survey %d closed. Thanks!";
    private static final String POLL_QUESTION_FMT = "(%d/%d) %s";
    private static final int DISPLAY_INDEX_OFFSET = 1;   // questionId -> 1-based

    private final TelegramGateway gateway;
    private final Community community;

    public TelegramSurveySender(TelegramGateway gateway, Community community) {
        this.gateway = gateway;
        this.community = community;
    }

    @Override
    public void sendSurvey(Survey survey) throws Exception {
        for (long chatId : community.getAllChatIds()) {
            trySendMessage(chatId, MSG_NEW_SURVEY_PREFIX + survey.getTitle());

            int total = survey.getQuestions().size();
            for (Question question : survey.getQuestions()) {
                List<String> opts = toOptionTexts(question);

                String pollTitle = String.format(
                        POLL_QUESTION_FMT,
                        question.getId() + DISPLAY_INDEX_OFFSET,
                        total,
                        question.getText()
                );

                SendPoll poll = new SendPoll(chatIdStr(chatId), pollTitle, opts);
                poll.setIsAnonymous(POLL_IS_ANONYMOUS);

                try {
                    Message msg = gateway.sendPoll(poll);
                } catch (TelegramApiException ignored) {
                }
            }
        }
    }

    @Override
    public void closeSurvey(long surveyId) throws Exception {
        for (long chatId : community.getAllChatIds()) {
            try {
                String text = String.format(MSG_CLOSED_FMT, surveyId);
                gateway.sendMessage(new SendMessage(chatIdStr(chatId), text));
            } catch (TelegramApiException ignored) {
            }
        }
    }

    private static String chatIdStr(long chatId) {
        return String.valueOf(chatId);
    }

    private static List<String> toOptionTexts(Question q) {
        List<String> opts = new ArrayList<>();
        for (OptionForQuestion option : q.getOptions()) {
            opts.add(option.getText());
        }
        return opts;
    }

    private void trySendMessage(long chatId, String text) {
        try {
            gateway.sendMessage(new SendMessage(chatIdStr(chatId), text));
        } catch (TelegramApiException ignored) {
        }
    }
}
