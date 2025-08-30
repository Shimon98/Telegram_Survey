package org.example;

import org.example.model.OptionForQuestion;
import org.example.model.Question;

import java.util.List;

public class SurveyResultFormatter {
    private static final String TITLE_RESULTS = "Survey results:";
    private static final String TITLE_QUESTION_FMT = "Q%d: %s";
    private static final String LINE_OPTION_FMT = " - %s: %d (%d%%)";

    public String buildSummary(SurveyState state, SurveyResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(TITLE_RESULTS).append("\n\n");

        if (state == null || state.getCurrentSurvey() == null ||
                state.getCurrentSurvey().getQuestions() == null ||
                state.getCurrentSurvey().getQuestions().isEmpty()) {
            sb.append("(no questions)").append("\n");
            return sb.toString().trim();
        }

        int qIndex = 0;
        while (qIndex < state.getCurrentSurvey().getQuestions().size()) {
            Question q = state.getCurrentSurvey().getQuestions().get(qIndex);
            List<OptionForQuestion> opts = q.getOptions();

            int[] counts = result.getOptionCountsForQuestion(qIndex);
            int totalAnswered = result.getTotalAnsweredForQuestion(qIndex);

            sb.append(String.format(TITLE_QUESTION_FMT, qIndex + 1, q.getText())).append("\n");

            if (counts == null) counts = new int[0];
            int n = counts.length;


            Integer[] idx = new Integer[n];
            int i = 0; while (i < n) { idx[i] = i; i++; }

            int a = 0;
            while (a < n - 1) {
                int b = 0;
                while (b < n - a - 1) {
                    int i1 = idx[b], i2 = idx[b+1];
                    if (counts[i1] < counts[i2]) {
                        int tmp = idx[b]; idx[b] = idx[b+1]; idx[b+1] = tmp;
                    }
                    b++;
                }
                a++;
            }


            int k = 0;
            while (k < n) {
                int pos = idx[k];
                String optText = (opts != null && pos < opts.size())
                        ? opts.get(pos).getText()
                        : ("Option " + (pos + 1));
                int c = counts[pos];
                int pct = (totalAnswered == 0) ? 0 : (c * 100) / totalAnswered;
                sb.append(String.format(LINE_OPTION_FMT, optText, c, pct)).append("\n");
                k++;
            }

            sb.append("\n");
            qIndex++;
        }
        return sb.toString().trim();
    }
}
