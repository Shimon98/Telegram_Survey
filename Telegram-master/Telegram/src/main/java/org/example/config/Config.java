package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String KEY_BOT_TOKEN = "BOT_TOKEN";
    private static final String KEY_BOT_USERNAME = "BOT_USERNAME";
    private static final String DEFAULT_USERNAME = "SurveyBot";
    private static final String KEY_MIN_COMMUNITY = "MIN_COMMUNITY_FOR_SURVEY";
    private static final String KEY_SURVEY_DURATION = "SURVEY_DURATION_MS";
    private static final int DEFAULT_MIN_COMMUNITY = 3;
    private static final long DEFAULT_SURVEY_DURATION = 300000L;
    private static final String CONFIG_RESOURCE = "config.properties";
    private static final String ERROR_MISSING_TOKEN = "BOT_TOKEN is missing. Set it in src/main/resources/config.properties";
    private static final String ERROR_LOAD_FAIL = "Failed to load ";

    private static Properties PROPS;

    private Config() {}

    private static Properties props() {
        if (PROPS != null) return PROPS;
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_RESOURCE)) {
            Properties p = new Properties();
            if (in != null) p.load(in);
            PROPS = p;
            return PROPS;
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_LOAD_FAIL + CONFIG_RESOURCE, e);
        }
    }

    private static String get(String key) {
        String v = props().getProperty(key);
        return v == null ? null : v.trim();
    }

    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    public static String getBotToken() {
        String token = get(KEY_BOT_TOKEN);
        if (isBlank(token)) throw new IllegalStateException(ERROR_MISSING_TOKEN);
        return token;
    }

    public static String getBotUsername() {
        String username = get(KEY_BOT_USERNAME);
        return isBlank(username) ? DEFAULT_USERNAME : username.trim();
    }

    public static int getMinCommunityForSurvey() {
        String v = get(KEY_MIN_COMMUNITY);
        if (isBlank(v)) return DEFAULT_MIN_COMMUNITY;
        try { int x = Integer.parseInt(v); return x < 1 ? DEFAULT_MIN_COMMUNITY : x; }
        catch (Exception ignore) { return DEFAULT_MIN_COMMUNITY; }
    }

    public static long getSurveyDurationMs() {
        String v = get(KEY_SURVEY_DURATION);
        if (isBlank(v)) return DEFAULT_SURVEY_DURATION;
        try { long x = Long.parseLong(v); return x < 1000L ? DEFAULT_SURVEY_DURATION : x; }
        catch (Exception ignore) { return DEFAULT_SURVEY_DURATION; }
    }
}
