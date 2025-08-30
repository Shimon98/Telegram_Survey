package org.example.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SurveyResult {
    private static final int ZERO = 0;

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
            who = new HashSet();
            this.answeredUserIdsByQuestionIndex.put(questionIndex, who);
        }
        who.add(userId);
    }

    public void incrementOptionCount(int questionIndex, int optionIndex) {
        int[] counts = this.optionCountsByQuestionIndex.get(questionIndex);
        if (counts == null) return;
        if (optionIndex < ZERO || optionIndex >= counts.length) return;
        int current = counts[optionIndex];
        counts[optionIndex] = current + 1;
    }

    public boolean everyoneAnsweredAllQuestions(int communitySizeAtStart) {
        for (Map.Entry<Integer, Set<Long>> e : this.answeredUserIdsByQuestionIndex.entrySet()) {
            Set<Long> whoAnswered = e.getValue();
            int answeredCount = (whoAnswered == null) ? ZERO : whoAnswered.size();
            if (answeredCount < communitySizeAtStart) {
                return false;
            }
        }
        return true;
    }



    public int[] getOptionCountsForQuestion(int questionIndex) {
        int[] arr = this.optionCountsByQuestionIndex.get(questionIndex);
        if (arr == null) return new int[ZERO];
        return arr;
    }

    public int getTotalAnsweredForQuestion(int questionIndex) {
        Set<Long> who = this.answeredUserIdsByQuestionIndex.get(questionIndex);
        return (who == null) ? ZERO : who.size();
    }
}
