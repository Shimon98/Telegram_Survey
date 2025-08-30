package org.example;

import org.example.bot.TelegramGateway;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class TelegramPollEngine {
    private static final boolean FALSE = false;

    private TelegramGateway telegramGateway;


    public TelegramPollEngine(TelegramGateway telegramGateway) {
        this.telegramGateway = telegramGateway;
    }

    public PollSendResult sendQuestionPoll(long chatId, String questionText, List<String> optionTexts) throws TelegramApiException {
        SendPoll poll = new SendPoll(String.valueOf(chatId), questionText, optionTexts);
        poll.setIsAnonymous(FALSE);
        poll.setAllowMultipleAnswers(FALSE);
        Message msg = this.telegramGateway.sendPoll(poll);
        int messageId = msg.getMessageId();
        String pollId = null;
        if (msg.getPoll() != null) {
            pollId = msg.getPoll().getId();
        }
        return new PollSendResult(messageId, pollId);
    }

    public void stopPollByMessageId(long chatId, int messageId) {
        try {
            Poll p = this.telegramGateway.stopPoll(new StopPoll(String.valueOf(chatId), messageId));
        } catch (TelegramApiException e) {

        }
    }
    public void sendTextToChat(long chatId, String text) {
        try {
            SendMessage msg = new SendMessage(String.valueOf(chatId), text);
            this.telegramGateway.sendMessage(msg);
        } catch (TelegramApiException e) {

        }
    }
}
