package org.example;
import org.example.model.Survey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurveyState {
    private boolean isSurveyOpen;
    private Survey currentSurvey;
    private int communitySizeAtStart;
    private Map<String, Integer> pollIdToQuestionIndex;
    private Map<Long, Map<Integer, Integer>> messageIdByChatIdAndQuestion;
    private Map<Integer, int[]> optionCountsByQuestionIndex;
    private Map<Integer, Set<Long>> answeredUserIdsByQuestionIndex;

    public SurveyState() {
        this.isSurveyOpen = false;
        this.currentSurvey = null;
        this.communitySizeAtStart = 0;
        this.pollIdToQuestionIndex = new HashMap<String, Integer>();
        this.messageIdByChatIdAndQuestion = new HashMap<Long, Map<Integer, Integer>>();
        this.optionCountsByQuestionIndex = new HashMap<Integer, int[]>();
        this.answeredUserIdsByQuestionIndex = new HashMap<Integer, Set<Long>>();
    }

    public void resetForNewSurvey(Survey survey, int communitySize) {
        this.isSurveyOpen = true;
        this.currentSurvey = survey;
        this.communitySizeAtStart = communitySize;
        this.pollIdToQuestionIndex.clear();
        this.messageIdByChatIdAndQuestion.clear();
        this.optionCountsByQuestionIndex.clear();
        this.answeredUserIdsByQuestionIndex.clear();
    }

    public void prepareQuestionBuckets(int questionIndex, int optionsCount) {
        this.optionCountsByQuestionIndex.put(questionIndex, new int[optionsCount]);
        this.answeredUserIdsByQuestionIndex.put(questionIndex, new HashSet<Long>());
    }

    public boolean isSurveyOpen() {
        return isSurveyOpen;
    }

    public Survey getCurrentSurvey() {
        return currentSurvey;
    }

    public int getCommunitySizeAtStart() {
        return communitySizeAtStart;
    }

    public Map<Long, Map<Integer, Integer>> getMessageIdByChatIdAndQuestion() {
        return messageIdByChatIdAndQuestion;
    }

    public Map<String, Integer> getPollIdToQuestionIndex() {
        return pollIdToQuestionIndex;
    }

    public Map<Integer, int[]> getOptionCountsByQuestionIndex() {
        return optionCountsByQuestionIndex;
    }

    public Map<Integer, Set<Long>> getAnsweredUserIdsByQuestionIndex() {
        return answeredUserIdsByQuestionIndex;
    }

    public void setSurveyOpen(boolean surveyOpen) {
        isSurveyOpen = surveyOpen;
    }

    public void setCurrentSurvey(Survey currentSurvey) {
        this.currentSurvey = currentSurvey;
    }

    public void setCommunitySizeAtStart(int communitySizeAtStart) {
        this.communitySizeAtStart = communitySizeAtStart;
    }

    public void setPollIdToQuestionIndex(Map<String, Integer> pollIdToQuestionIndex) {
        this.pollIdToQuestionIndex = pollIdToQuestionIndex;
    }

    public void setMessageIdByChatIdAndQuestion(Map<Long, Map<Integer, Integer>> messageIdByChatIdAndQuestion) {
        this.messageIdByChatIdAndQuestion = messageIdByChatIdAndQuestion;
    }

    public void setOptionCountsByQuestionIndex(Map<Integer, int[]> optionCountsByQuestionIndex) {
        this.optionCountsByQuestionIndex = optionCountsByQuestionIndex;
    }

    public void setAnsweredUserIdsByQuestionIndex(Map<Integer, Set<Long>> answeredUserIdsByQuestionIndex) {
        this.answeredUserIdsByQuestionIndex = answeredUserIdsByQuestionIndex;
    }
}
