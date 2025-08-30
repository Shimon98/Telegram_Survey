package org.example.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.polls.SendPoll;
import org.telegram.telegrambots.meta.api.methods.polls.StopPoll;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.polls.Poll;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class TelegramGateway {
    private TelegramLongPollingBot bot;

    public TelegramGateway(TelegramLongPollingBot bot) {
        this.bot = bot;
    }

    public Message sendMessage(SendMessage msg) throws TelegramApiException {
        return bot.execute(msg);
    }

    public Message sendPoll(SendPoll poll) throws TelegramApiException {
        return bot.execute(poll);
    }

    public Poll stopPoll(StopPoll stop) throws TelegramApiException {
        return bot.execute(stop);
    }
}
