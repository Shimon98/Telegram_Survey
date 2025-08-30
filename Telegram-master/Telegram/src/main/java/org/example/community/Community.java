package org.example.community;

import org.example.model.Member;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Community {
    private CommunityRegistry registry;

    public Community(CommunityRegistry registry) {
        this.registry = registry;
    }

    public int size() {
        return registry.getMembers().size();
    }

    public Set<Long> getAllChatIds() {
        return registry.getMembers().stream().map(Member::getChatId).collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Long> getAllChetId() {
        return getAllChatIds();
    }
}
