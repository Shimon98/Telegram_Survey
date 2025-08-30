package org.example.community;

import org.example.util.JoinOutcome;


public class CommunityService {
    private CommunityRegistry registry;

    public CommunityService(CommunityRegistry registry) {
        this.registry = registry;
    }

    public JoinOutcome handleRegister(long chatId, String name, String text) {
        return registry.registerIfJoinTrigger(chatId, name, text);
    }
}
