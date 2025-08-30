package org.example.engine;

import org.example.model.OptionForQuestion;
import org.example.model.Survey;
import org.example.SurveyState;

import java.util.List;

public class CountersPreparer {
    private SurveyState surveyState;
    private SurveyLocks locks;

    public CountersPreparer(SurveyState surveyState, SurveyLocks locks) {
        this.surveyState = surveyState;
        this.locks = locks;
    }

    public void prepareForAllQuestions(Survey survey) {
        int numQuestions = survey.getQuestions().size();
        int qIndex = 0;
        while (qIndex < numQuestions) {
            List<OptionForQuestion> options = survey.getQuestions().get(qIndex).getOptions();
            int optionsCount = (options == null) ? 0 : options.size();
            synchronized (this.locks.getStateLock()) {
                this.surveyState.prepareQuestionBuckets(qIndex, optionsCount);
            }
            qIndex = qIndex + 1;
        }
    }
}
