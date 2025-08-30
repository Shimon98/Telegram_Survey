package org.example.community;


import org.example.engine.TelegramPollEngine;

import java.util.Set;

public class CommunityBroadcaster {

    private static final String LOG_PREFIX = "[CommunityBroadcaster] ";
    private static final String LOG_BROADCAST = LOG_PREFIX + "Broadcast to all members.";

    private Community community;
    private TelegramPollEngine pollEngine;

    public CommunityBroadcaster(Community community, TelegramPollEngine pollEngine) {
        this.community = community;
        this.pollEngine = pollEngine;
    }

    public void broadcast(String text) {
        System.out.println(LOG_BROADCAST);
        Set<Long> allChatIds = this.community.getAllChatIds();
        for (Long chatId : allChatIds) {
            this.pollEngine.sendTextToChat(chatId, text);
        }
    }
}
