package org.example.engine;

import org.example.model.OptionForQuestion;
import org.example.model.Question;
import org.example.model.Survey;

import java.util.List;

public class CountersPreparer {
    private static final int ZERO = 0;
    private static final int ONE = 1;


    public static void prepare(SurveyState state, Survey survey) {
        if (state == null || survey == null) {
            return;
        }

        List<Question> questions = survey.getQuestions();
        if (questions == null || questions.isEmpty()) {
            return;
        }

        for (int i = ZERO; i < questions.size(); i += ONE) {
            int count = optionsCount(questions.get(i));
            state.prepareQuestionBuckets(i, count);
        }
    }

    private static int optionsCount(Question q) {
        if (q == null) {
            return ZERO;
        }
        List<OptionForQuestion> opts = q.getOptions();
        if (opts == null) {
            return ZERO;
        }
        return opts.size();
    }
}
