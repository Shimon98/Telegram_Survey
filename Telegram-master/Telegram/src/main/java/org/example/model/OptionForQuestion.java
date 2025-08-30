package org.example.model;

import org.example.util.Validate;

import java.util.Objects;

public class OptionForQuestion {
    private static final String ERROR_INDEX="Option index";
    private static final String ERROR_TEXT="Option text";


    private String text;
    private int indexId;


    private OptionForQuestion(int indexId, String text) {
        this.indexId = indexId;
        this.text = text;
    }

    public static OptionForQuestion create(int oneBasedIndex, String text) {
        String t = Validate.requireText(text,ERROR_TEXT);
        int index = Validate.requirePositiveOrZero(oneBasedIndex,ERROR_INDEX);
        return new OptionForQuestion(index, t);
    }

    public String getText() {
        return text;
    }

    public int getId() {
        return indexId;
    }

    @Override
    public String toString() {
        return "OptionForQuestion{" +
                "text='" + text + '\'' +
                ", indexId=" + indexId +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof OptionForQuestion)) return false;
        OptionForQuestion option = (OptionForQuestion) object;
        return indexId == option.indexId && text.equals(option.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.indexId, this.text);
    }
}
