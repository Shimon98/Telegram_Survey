package org.example.core;

import org.example.model.Survey;

public interface ResultsAggregator {
    void recordVote(int questionId, int optionIndex, long memberId);

    boolean hasMemberCompletedAll(long memberId, Survey survey);

    SurveyResult buildResult(Survey survey);
}
