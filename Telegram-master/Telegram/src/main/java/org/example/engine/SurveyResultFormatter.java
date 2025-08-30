package org.example.engine;

import org.example.model.OptionForQuestion;
import org.example.model.Question;

import java.util.List;

public class SurveyResultFormatter {
    private static final String TITLE_RESULTS = "Survey results:";
    private static final String TITLE_QUESTION_FMT = "Q%d: %s";
    private static final String LINE_OPTION_FMT = " - %s: %d (%d%%)";
    private static final String NO_QUESTIONS_TEXT = "(no questions)";
    private static final String DEFAULT_OPTION_PREFIX = "Option ";
    private static final String NEW_LINE = "\n";
    private static final String DOUBLE_NEW_LINE = "\n\n";
    private static final int ONE = 1;
    private static final int PERCENT_BASE = 100;
    private static final int ZERO = 0;

    public String buildSummary(SurveyState state, SurveyResult result) {
        StringBuilder sb = new StringBuilder();
        appendHeader(sb);

        if (hasNoQuestions(state)) {
            appendNoQuestions(sb);
            return trimToString(sb);
        }
        int questionCount = state.getCurrentSurvey().getQuestions().size();
        int qIndex = ZERO;
        while (qIndex < questionCount) {
            appendQuestionBlock(sb, state, result, qIndex);
            qIndex++;
        }
        return trimToString(sb);
    }

    private void appendHeader(StringBuilder sb) {
        sb.append(TITLE_RESULTS).append(DOUBLE_NEW_LINE);
    }

    private boolean hasNoQuestions(SurveyState state) {
        return state == null || state.getCurrentSurvey() == null
                || state.getCurrentSurvey().getQuestions() == null
                || state.getCurrentSurvey().getQuestions().isEmpty();
    }

    private void appendNoQuestions(StringBuilder stringBuilder) {
        stringBuilder.append(NO_QUESTIONS_TEXT).append(NEW_LINE);
    }

    private void appendQuestionBlock(StringBuilder sb, SurveyState state, SurveyResult result, int qIndex) {
        Question question = state.getCurrentSurvey().getQuestions().get(qIndex);
        List<OptionForQuestion> opts = question.getOptions();

        int[] counts = safeCounts(result.getOptionCountsForQuestion(qIndex));
        int totalAnswered = result.getTotalAnsweredForQuestion(qIndex);
        appendQuestionTitle(sb, qIndex, question.getText());
        Integer[] sortedIdx = sortIndicesByCountsDesc(counts);
        appendOptionsLines(sb, sortedIdx, counts, totalAnswered, opts);
        sb.append(NEW_LINE);
    }


    private void appendQuestionTitle(StringBuilder builder, int qIndex, String questionText) {
        builder.append(String.format(TITLE_QUESTION_FMT, qIndex + ONE, nullToEmpty(questionText))).append(NEW_LINE);
    }


    private void appendOptionsLines(StringBuilder sb,
                                    Integer[] idx,
                                    int[] counts,
                                    int totalAnswered,
                                    List<OptionForQuestion> opts) {
        int k = ZERO;
        int n = idx.length;
        while (k < n) {
            int pos = idx[k];
            String optText = resolveOptionText(opts, pos);
            int c = counts[pos];
            int pct = computePercent(c, totalAnswered);
            appendOptionLine(sb, optText, c, pct);
            k++;
        }
    }

    private void appendOptionLine(StringBuilder sb, String optText, int count, int percent) {
        sb.append(String.format(LINE_OPTION_FMT, optText, count, percent)).append(NEW_LINE);
    }


    private int[] safeCounts(int[] counts) {
        return (counts == null) ? new int[ZERO] : counts;
    }

    private String resolveOptionText(List<OptionForQuestion> opts, int pos) {
        if (opts != null && pos < opts.size()) {
            String t = opts.get(pos).getText();
            return (t == null) ? defaultOptionLabel(pos) : t;
        }
        return defaultOptionLabel(pos);
    }

    private String defaultOptionLabel(int pos) {
        return DEFAULT_OPTION_PREFIX + (pos + ONE);
    }

    private int computePercent(int count, int total) {
        if (total == ZERO) return ZERO;
        return (count * PERCENT_BASE) / total;
    }

    private String nullToEmpty(String s) {
        return (s == null) ? "" : s;
    }

    private String trimToString(StringBuilder sb) {
        return sb.toString().trim();
    }


    private Integer[] sortIndicesByCountsDesc(int[] counts) {
        int n = counts.length;
        Integer[] idX = new Integer[n];
        int i = ZERO;
        while (i < n) {
            idX[i] = i;
            i++;
        }

        int a = ZERO;
        while (a < n - ONE) {
            int b = ZERO;
            while (b < n - a - ONE) {
                int i1 = idX[b], i2 = idX[b + ONE];
                if (counts[i1] < counts[i2]) {
                    int tmp = idX[b];
                    idX[b] = idX[b + ONE];
                    idX[b + ONE] = tmp;
                }
                b++;
            }
            a++;
        }
        return idX;
    }
}
