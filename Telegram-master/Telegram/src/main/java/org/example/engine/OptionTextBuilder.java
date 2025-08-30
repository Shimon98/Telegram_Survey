package org.example.engine;

import org.example.model.OptionForQuestion;

import java.util.ArrayList;
import java.util.List;

public class OptionTextBuilder {

    public List<String> buildOptionTexts(List<OptionForQuestion> options) {
        List<String> out = new ArrayList<String>();
        if (options == null) return out;
        int i = 0;
        while (i < options.size()) {
            out.add(options.get(i).getText());
            i = i + 1;
        }
        return out;
    }
}
