package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_RESOURCE = "config.properties";
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String KEY_MIN_COMMUNITY = "MIN_COMMUNITY_FOR_SURVEY";
    private static final String KEY_SURVEY_DURATION = "SURVEY_DURATION_MS";
    private static final String DEFAULT_USERNAME = "SurveyBot";
    private static final int DEFAULT_MIN_COMMUNITY = 3;
    private static final int MIN_ALLOWED_COMMUNITY = 1;
    private static final long DEFAULT_SURVEY_DURATION_MS = 300_000L;
    private static final long MIN_ALLOWED_SURVEY_DURATION_MS = 1_000L;
    private static final String ERROR_MISSING_TOKEN =
            "BOT_TOKEN is missing. Set it in src/main/resources/config.properties";
    private static final String ERROR_LOAD_FAIL_PREFIX = "Failed to load ";


    private static Properties PROPS;

    public static String getBotToken() {
        String token = read(KEY_BOT_TOKEN);
        if (isBlank(token)) {
            throw new IllegalStateException(ERROR_MISSING_TOKEN);
        }
        return token;
    }

    public static String getBotUsername() {
        String username = read(KEY_BOT_USERNAME);
        if (isBlank(username)) {
            return DEFAULT_USERNAME;
        }
        return username;
    }

    public static int getMinCommunityForSurvey() {
        String value = read(KEY_MIN_COMMUNITY);
        return parseIntWithMinOrDefault(value, DEFAULT_MIN_COMMUNITY, MIN_ALLOWED_COMMUNITY);
    }

    public static long getSurveyDurationMs() {
        String value = read(KEY_SURVEY_DURATION);
        return parseLongWithMinOrDefault(value, DEFAULT_SURVEY_DURATION_MS, MIN_ALLOWED_SURVEY_DURATION_MS);
    }

    private static Properties props() {
        if (PROPS != null) {
            return PROPS;
        }
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            Properties p = new Properties();
            if (in != null) {
                p.load(in);
            }
            PROPS = p;
            return PROPS;
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_LOAD_FAIL_PREFIX + CONFIG_RESOURCE, e);
        }
    }

    private static String read(String key) {
        String v = props().getProperty(key);
        if (v == null) {
            return null;
        }
        return v.trim();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static int parseIntWithMinOrDefault(String s, int defaultValue, int minAllowed) {
        if (isBlank(s)) {
            return defaultValue;
        }
        try {
            int x = Integer.parseInt(s.trim());
            if (x < minAllowed) {
                return defaultValue;
            }
            return x;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static long parseLongWithMinOrDefault(String s, long defaultValue, long minAllowed) {
        if (isBlank(s)) {
            return defaultValue;
        }
        try {
            long x = Long.parseLong(s.trim());
            if (x < minAllowed) {
                return defaultValue;
            }
            return x;
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
