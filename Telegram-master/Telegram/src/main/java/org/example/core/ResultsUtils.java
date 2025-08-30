package org.example.core;

import java.util.ArrayList;
import java.util.List;

public class ResultsUtils {


    public static List<SurveyResult.OptionResult> sortByVotesDesc(List<SurveyResult.OptionResult> in) {
        List<SurveyResult.OptionResult> copy = new ArrayList<>(in);
        copy.sort((a, b) -> Integer.compare(b.getVotes(), a.getVotes()));
        return copy;
    }

    public static String percent(int votes, int total) {
        if (total <= 0) {
            return "0%";
        }
        int p = Math.round((votes * 100f) / (float) total);
        return p + "%";
    }
}
