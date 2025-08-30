package org.example.bot;

import org.example.SurveyEngine;
import org.example.community.CommunityRegistry;
import org.example.community.CommunityService;
import org.example.config.Config;
import org.example.util.JoinOutcome;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Objects;

public class Bot extends TelegramLongPollingBot {
    private static final String LOG_PREFIX = "[Bot] ";
    private static final String EMOJI_JOINED = "🟢 ";
    private static final String EMOJI_WELCOME = "✅ ";
    private static final String USER_FALLBACK_PREFIX = "User ";
    private static final String NEW_LINE = System.lineSeparator();
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    private static final String NOTE_JOINED_FMT = EMOJI_JOINED + "%s joined. Community size: %d";
    private static final String WELCOME_FMT = EMOJI_WELCOME + "Welcome, %s!" + NEW_LINE + "Community size: %d";
    private static final String LOG_ALREADY_MEMBER_FMT = LOG_PREFIX + "Member already registered: %d";
    private static final String LOG_SEND_FAIL_FMT = LOG_PREFIX + "Failed to send message to chatId=%d: %s";

    private static final String ERR_COMMUNITY_SERVICE_NULL = "communityService is null";
    private static final String ERR_REGISTRY_NULL = "registry is null";

    private CommunityService communityService;
    private CommunityRegistry registry;
    private SurveyEngine surveyEngine;

    public Bot(CommunityService communityService, CommunityRegistry registry) {
        this.communityService = Objects.requireNonNull(communityService, ERR_COMMUNITY_SERVICE_NULL);
        this.registry = Objects.requireNonNull(registry, ERR_REGISTRY_NULL);
    }

    public void setSurveyEngine(SurveyEngine surveyEngine) {
        this.surveyEngine = surveyEngine;
    }

    @Override
    public String getBotToken() {
        return Config.getBotToken();
    }

    @Override
    public String getBotUsername() {
        return Config.getBotUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (isPollAnswer(update)) {
            if (surveyEngine != null) {
                surveyEngine.onPollAnswer(update.getPollAnswer());
            }
            return;
        }

        if (!isTextMessage(update)) {
            return;
        }

        long chatId = update.getMessage().getChatId();
        String fullName = extractFullName(update);
        String text = update.getMessage().getText();

        JoinOutcome outcome = communityService.handleRegister(chatId, fullName, text);
        handleJoinOutcome(outcome, chatId, fullName);
    }


    private void handleJoinOutcome(JoinOutcome outcome, long chatId, String fullName) {
        if (outcome == null) {
            return;
        }

        switch (outcome) {
            case ADDED_NEW_MEMBER: {
                int size = registry.getMembers().size();
                String joinedName = displayName(chatId, fullName);
                notifyCommunityJoined(chatId, joinedName, size);
                welcomeNewMember(chatId, joinedName, size);
                break;
            }
            case ALREADY_MEMBER: {
                log(String.format(LOG_ALREADY_MEMBER_FMT, chatId));
                break;
            }
            default: {
                break;
            }
        }
    }

    private void notifyCommunityJoined(long newMemberChatId, String joinedName, int size) {
        String note = String.format(NOTE_JOINED_FMT, joinedName, size);
        for (var member : registry.getMembers()) {
            if (member.getChatId() == newMemberChatId) {
                continue;
            }
            trySend(member.getChatId(), note);
        }
    }

    private void welcomeNewMember(long chatId, String joinedName, int size) {
        String welcome = String.format(WELCOME_FMT, joinedName, size);
        trySend(chatId, welcome);
    }

    private static boolean isTextMessage(Update update) {
        return update != null && update.hasMessage() && update.getMessage().hasText();
    }

    private static boolean isPollAnswer(Update update) {
        return update != null && update.hasPollAnswer();
    }

    private static String extractFullName(Update update) {
        if (update == null || update.getMessage() == null || update.getMessage().getFrom() == null) {
            return null;
        }
        String first = safeTrim(update.getMessage().getFrom().getFirstName());
        String last = safeTrim(update.getMessage().getFrom().getLastName());
        String full = (first + SPACE + last).trim();
        if (full.equals(EMPTY)) {
            return null;
        }
        return full;
    }

    private static String safeTrim(String s) {
        if (s == null) {
            return EMPTY;
        }
        return s.trim();
    }

    private static String displayName(long chatId, String name) {
        if (name == null || name.isBlank()) {
            return USER_FALLBACK_PREFIX + chatId;
        }
        return name;
    }

    private void trySend(long chatId, String text) {
        try {
            execute(new SendMessage(String.valueOf(chatId), text));
        } catch (TelegramApiException e) {
            log(String.format(LOG_SEND_FAIL_FMT, chatId, e.getMessage()));
        }
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}
