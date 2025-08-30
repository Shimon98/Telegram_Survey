package org.example.model;

import org.example.util.HasId;
import org.example.util.HasText;
import org.example.util.Validate;

import java.util.ArrayList;
import java.util.List;

public class Question implements HasId, HasText {
    private static final String ERROR_INDEX = "Question index";
    private static final String ERROR_TEXT = "Question text";
    private static final String ERROR_SIZE = "List of options for Question ";
    private static final Integer MIN_OPTION = 2;
    private static final Integer MAX_OPTION = 4;


    private int id;
    private String text;
    private List<OptionForQuestion> options;

    private Question(int id, String text, List<String> optionInformationOnText) {
        this.id = id;
        this.text = text;
        this.options = generatesAnOptionFromText(optionInformationOnText);
    }

    public static Question create(int id, String text, List<String> optionInformationOnText) {
        String t = Validate.requireText(text, ERROR_TEXT);
        int indexId = Validate.requirePositiveOrZero(id, ERROR_INDEX);
        Validate.requireSizeBetween(optionInformationOnText, MIN_OPTION, MAX_OPTION, ERROR_SIZE);
        return new Question(indexId, t, optionInformationOnText);
    }


    private List<OptionForQuestion> generatesAnOptionFromText(List<String> optionInformationOnText) {
        int conter = 0;
        List<OptionForQuestion> newOptions = new ArrayList<>();
        for (String text : optionInformationOnText) {
            newOptions.add(OptionForQuestion.create(conter, text));
            conter++;
        }
        return newOptions;

    }

    public int getId() {
        return this.id;
    }


    public String getText() {
        return this.text;
    }

    public List<OptionForQuestion> getOptions() {
        return this.options;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", options=" + options +
                '}';
    }


}
