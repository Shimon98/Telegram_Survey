package org.example.engine;
import org.example.model.Survey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurveyState {
    private static final Integer START_SIZE=0;

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
        this.communitySizeAtStart = START_SIZE;
        this.pollIdToQuestionIndex = new HashMap();
        this.messageIdByChatIdAndQuestion = new HashMap();
        this.optionCountsByQuestionIndex = new HashMap();
        this.answeredUserIdsByQuestionIndex = new HashMap();
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


}
