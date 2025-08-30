package org.example.community;

import org.example.model.Member;
import org.example.util.JoinOutcome;
import org.example.util.JoinTrigger;

import java.util.HashSet;
import java.util.Set;


public class CommunityRegistry {
    private static final String NULL_NAME = "Anonymous ";

    private Set<Member> members;
    private int anonCounter;

    public CommunityRegistry() {
        this.members = new HashSet<>();
        this.anonCounter = 0;
    }

    public synchronized JoinOutcome registerIfJoinTrigger(long chatId, String nameOrNull, String text) {
        if (!JoinTrigger.matches(text)) {
            return JoinOutcome.IGNORED;
        }
        Member member = new Member(chatId, nameOrNull == null || nameOrNull.isBlank() ? NULL_NAME + (anonCounter++) : nameOrNull);
        boolean added = members.add(member);
        return added ? JoinOutcome.ADDED_NEW_MEMBER : JoinOutcome.ALREADY_MEMBER;
    }

    public synchronized Set<Member> getMembers() {
        return new HashSet<>(members);
    }
}
