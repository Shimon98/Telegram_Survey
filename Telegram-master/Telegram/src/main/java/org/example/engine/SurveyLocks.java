package org.example.engine;

public class SurveyLocks {
    private Object stateLock;

    public SurveyLocks() {
        this.stateLock = new Object();
    }

    public Object getStateLock() {
        return this.stateLock;
    }
}
