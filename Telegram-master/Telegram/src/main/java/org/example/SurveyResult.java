package org.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurveyResult {

    private Map<Integer, int[]> optionCountsByQuestionIndex;
    private Map<Integer, Set<Long>> answeredUserIdsByQuestionIndex;


    public SurveyResult(Map<Integer, int[]> optionCountsByQuestionIndex,
                        Map<Integer, Set<Long>> answeredUserIdsByQuestionIndex) {
        this.optionCountsByQuestionIndex = optionCountsByQuestionIndex;
        this.answeredUserIdsByQuestionIndex = answeredUserIdsByQuestionIndex;
    }

    public boolean didUserAlreadyAnswer(int questionIndex, long userId) {
        Set<Long> who = this.answeredUserIdsByQuestionIndex.get(questionIndex);
        if (who == null) return false;
        return who.contains(userId);
    }

    public void markUserAnswered(int questionIndex, long userId) {
        Set<Long> who = this.answeredUserIdsByQuestionIndex.get(questionIndex);
        if (who == null) {
            who = new HashSet<Long>();
            this.answeredUserIdsByQuestionIndex.put(questionIndex, who);
        }
        who.add(userId);
    }

    public void incrementOptionCount(int questionIndex, int optionIndex) {
        int[] counts = this.optionCountsByQuestionIndex.get(questionIndex);
        if (counts == null) return;
        if (optionIndex < 0 || optionIndex >= counts.length) return;
        int current = counts[optionIndex];
        counts[optionIndex] = current + 1;
    }

    public boolean everyoneAnsweredAllQuestions(int communitySizeAtStart) {
        for (Map.Entry<Integer, Set<Long>> e : this.answeredUserIdsByQuestionIndex.entrySet()) {
            Set<Long> whoAnswered = e.getValue();
            int answeredCount = (whoAnswered == null) ? 0 : whoAnswered.size();
            if (answeredCount < communitySizeAtStart) {
                return false;
            }
        }
        return true;
    }

    public Map<Integer, int[]> snapshotOptionCounts() {
        Map<Integer, int[]> copy = new HashMap<Integer, int[]>();
        for (Map.Entry<Integer, int[]> e : this.optionCountsByQuestionIndex.entrySet()) {
            int[] src = e.getValue();
            if (src == null) continue;
            int[] dst = new int[src.length];
            int i = 0;
            while (i < src.length) {
                dst[i] = src[i];
                i = i + 1;
            }
            copy.put(e.getKey(), dst);
        }
        return copy;
    }

    public int[] getOptionCountsForQuestion(int questionIndex) {
        int[] arr = this.optionCountsByQuestionIndex.get(questionIndex);
        if (arr == null) return new int[0];
        return arr;
    }

    public int getTotalAnsweredForQuestion(int questionIndex) {
        Set<Long> who = this.answeredUserIdsByQuestionIndex.get(questionIndex);
        return (who == null) ? 0 : who.size();
    }
}
