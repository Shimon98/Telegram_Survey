package org.example.model;

import java.util.Objects;

public class Member {
    private static final String DEFAULT_USERNAME = "Anonymous";

    private long chatId;
    private String userName;

    public Member(long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName == null ? DEFAULT_USERNAME : userName;
    }

    public long getChatId() {
        return chatId;
    }



    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Member)) return false;
        Member member = (Member) object;
        return chatId == member.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }

    @Override
    public String toString() {
        return userName + " (" + chatId + ")";
    }
}
