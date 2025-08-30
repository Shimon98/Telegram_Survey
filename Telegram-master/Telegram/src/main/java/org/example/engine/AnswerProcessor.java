package org.example.engine;

import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

import java.util.List;
import java.util.Objects;

public class AnswerProcessor {
    private static final int ZERO = 0;
    private static final int INVALID_INDEX = -1;
    private static final String ERR_STATE_NULL = "surveyState is null";
    private static final String ERR_RESULT_NULL = "surveyResult is null";
    private static final String ERR_LOCKS_NULL = "locks is null";

    private SurveyState surveyState;
    private SurveyResult surveyResult;
    private SurveyLocks locks;

    public AnswerProcessor(SurveyState surveyState, SurveyResult surveyResult, SurveyLocks locks) {
        this.surveyState = Objects.requireNonNull(surveyState, ERR_STATE_NULL);
        this.surveyResult = Objects.requireNonNull(surveyResult, ERR_RESULT_NULL);
        this.locks = Objects.requireNonNull(locks, ERR_LOCKS_NULL);
    }

    public boolean process(PollAnswer pollAnswer) {
        if (!hasValidPollAnswer(pollAnswer)) {
            return false;
        }

        String pollId = pollAnswer.getPollId();
        long userId = pollAnswer.getUser().getId();
        int chosenOptionIndex = firstSelectedOptionIndex(pollAnswer);

        if (chosenOptionIndex == INVALID_INDEX) {
            return false;
        }

        Object stateLock = locks.getStateLock();
        synchronized (stateLock) {
            if (!surveyState.isSurveyOpen()) {
                return false;
            }

            Integer questionIndex = surveyState.getPollIdToQuestionIndex().get(pollId);
            if (questionIndex == null) {
                return false;
            }

            if (surveyResult.didUserAlreadyAnswer(questionIndex, userId)) {
                return false;
            }

            surveyResult.markUserAnswered(questionIndex, userId);
            surveyResult.incrementOptionCount(questionIndex, chosenOptionIndex);

            int communitySizeAtStart = surveyState.getCommunitySizeAtStart();
            return surveyResult.everyoneAnsweredAllQuestions(communitySizeAtStart);
        }
    }

    private static boolean hasValidPollAnswer(PollAnswer pa) {
        if (pa == null) {
            return false;
        }
        if (pa.getPollId() == null || pa.getPollId().trim().isEmpty()) {
            return false;
        }
        User u = pa.getUser();
        if (u == null) {
            return false;
        }
        return true;
    }

    private static int firstSelectedOptionIndex(PollAnswer pa) {
        List<Integer> ids = pa.getOptionIds();
        if (ids == null || ids.isEmpty()) {
            return INVALID_INDEX;
        }
        Integer idx = ids.get(ZERO);
        if (idx == null) {
            return INVALID_INDEX;
        }
        return idx;
    }
}
