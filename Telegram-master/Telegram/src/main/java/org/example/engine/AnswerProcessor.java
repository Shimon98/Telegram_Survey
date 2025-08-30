package org.example.engine;

import org.example.SurveyResult;
import org.example.SurveyState;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

import java.util.List;

public class AnswerProcessor {
    private SurveyState surveyState;
    private SurveyResult surveyResult;
    private SurveyLocks locks;

    public AnswerProcessor(SurveyState surveyState, SurveyResult surveyResult, SurveyLocks locks) {
        this.surveyState = surveyState;
        this.surveyResult = surveyResult;
        this.locks = locks;
    }

    public boolean process(PollAnswer pollAnswer) {
        if (pollAnswer == null) return false;

        String pollId = pollAnswer.getPollId();
        long userId = pollAnswer.getUser().getId();

        int chosenOptionIndex = -1;
        List<Integer> ids = pollAnswer.getOptionIds();
        if (ids != null && !ids.isEmpty()) chosenOptionIndex = ids.get(0);
        if (chosenOptionIndex < 0) return false;

        synchronized (this.locks.getStateLock()) {
            if (!this.surveyState.isSurveyOpen()) return false;
            Integer questionIndex = this.surveyState.getPollIdToQuestionIndex().get(pollId);
            if (questionIndex == null) return false;
            if (this.surveyResult.didUserAlreadyAnswer(questionIndex, userId)) return false;
            this.surveyResult.markUserAnswered(questionIndex, userId);
            this.surveyResult.incrementOptionCount(questionIndex, chosenOptionIndex);

            return this.surveyResult.everyoneAnsweredAllQuestions(this.surveyState.getCommunitySizeAtStart());
        }
    }
}
