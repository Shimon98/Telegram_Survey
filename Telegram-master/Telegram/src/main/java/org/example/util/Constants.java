package org.example.util;

public final class Constants {
    private Constants() {}

    public static final int MIN_QUESTIONS = 1;
    public static final int MAX_QUESTIONS = 3;
    public static final int MIN_OPTIONS   = 2;
    public static final int MAX_OPTIONS   = 4;

    public static final int MIN_COMMUNITY_FOR_SURVEY = 3; // set to 1 when testing

    public static final long SURVEY_DURATION_MS = 5L * 60_000L;

    public static final String CARD_WELCOME  = "WELCOME";
    public static final String CARD_MANUAL   = "MANUAL";
    public static final String CARD_PREVIEW  = "PREVIEW";
    public static final String CARD_PROGRESS = "PROGRESS";
    public static final String CARD_RESULTS  = "RESULTS";
}
