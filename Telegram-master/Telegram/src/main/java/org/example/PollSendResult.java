package org.example;

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

    public void setPollId(String pollId) {
        this.pollId = pollId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}