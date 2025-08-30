package org.example.engine;

public class PollSendResult {
    private int messageId;
    private String pollId;

    public PollSendResult(int messageId, String pollId) {
        this.messageId = messageId;
        this.pollId = pollId;
    }

    public String getPollId() {
        return pollId;
    }
    public int getMessageId() {
        return messageId;
    }
}