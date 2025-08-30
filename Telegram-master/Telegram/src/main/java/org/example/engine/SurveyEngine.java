// file: org/example/SurveyEngine.java
package org.example;

import org.example.community.Community;
import org.example.community.CommunityBroadcaster;

import org.example.gui.SurveyTimeoutThread;
import org.example.model.Survey;
import org.telegram.telegrambots.meta.api.objects.polls.PollAnswer;

public class SurveyEngine {

    private static final String LOG_PREFIX = "[SurveyEngine] ";
    private static final String STARTING_SURVEY = "Starting survey.";
    private static final long MILLIS_PER_SECOND = 1000L;
    private static final long SECONDS_PER_MINUTE = 60L;
    private static final long DEFAULT_TIMEOUT_MINUTES = 5L;
    private static final long DEFAULT_TIMEOUT_MS =
            DEFAULT_TIMEOUT_MINUTES * SECONDS_PER_MINUTE * MILLIS_PER_SECOND;

    private static boolean IS_SURVEY_ACTIVE = false;

    private Community community;
    private TelegramPollEngine telegramPollEngine;
    private SurveyState surveyState;
    private SurveyResult surveyResult;
    private SurveyLocks surveyLocks;
    private SurveyPreconditions surveyPreconditions;
    private OptionTextBuilder optionTextBuilder;
    private MessageAndPollRegistry messageAndPollRegistry;
    private SurveySender surveySender;
    private AnswerProcessor answerProcessor;
    private SurveyCloser surveyCloser;
    private CommunityBroadcaster broadcaster;
    private SurveyResultFormatter resultFormatter;
    private SurveyFinisher surveyFinisher;

    public SurveyEngine(TelegramPollEngine telegramPollEngine, Community community) {
        this.telegramPollEngine = telegramPollEngine;
        this.community = community;

        this.surveyState = new SurveyState();
        this.surveyLocks = new SurveyLocks();
        this.surveyPreconditions = new SurveyPreconditions(this.community, this.surveyState);

        this.optionTextBuilder = new OptionTextBuilder();
        this.messageAndPollRegistry = new MessageAndPollRegistry(this.surveyState, this.surveyLocks);
        this.surveySender = new SurveySender(this.community, this.telegramPollEngine,
                this.optionTextBuilder, this.messageAndPollRegistry);

        this.broadcaster = new CommunityBroadcaster(this.community, this.telegramPollEngine);
        this.resultFormatter = new SurveyResultFormatter();
    }

    public void startSurvey(Survey survey) {
        this.surveyPreconditions.validateBeforeStart(survey);
        int communitySize = this.community.size();
        synchronized (this.surveyLocks.getStateLock()) {
            this.surveyState.resetForNewSurvey(survey, communitySize);
            this.surveyResult = new SurveyResult(
                    this.surveyState.getOptionCountsByQuestionIndex(),
                    this.surveyState.getAnsweredUserIdsByQuestionIndex()
            );
            this.answerProcessor = new AnswerProcessor(this.surveyState, this.surveyResult, this.surveyLocks);
            this.surveyCloser = new SurveyCloser(this.telegramPollEngine, this.surveyState, this.surveyLocks);
            this.surveyFinisher = new SurveyFinisher(this.surveyCloser, this.surveyState,
                    this.surveyResult, this.resultFormatter, this.broadcaster);
        }

        CountersPreparer.prepare(this.surveyState, survey);

        System.out.println(LOG_PREFIX + STARTING_SURVEY);
        this.surveySender.sendAllQuestionsToAllMembers(survey);

        SurveyTimeoutThread timeoutThread =
                new SurveyTimeoutThread(this.surveyState, this.surveyFinisher, DEFAULT_TIMEOUT_MS);
        timeoutThread.setDaemon(true);
        timeoutThread.start();

        IS_SURVEY_ACTIVE = true;
    }

    public void onPollAnswer(PollAnswer pollAnswer) {
        boolean allAnswered = this.answerProcessor.process(pollAnswer);
        if (allAnswered) {
            this.surveyFinisher.finishAllAnswered();
        }
    }

    public void closeActiveSurvey() {
        this.surveyFinisher.finishTimeout();
    }



    public boolean isSurveyOpen() {
        return this.surveyState.isSurveyOpen();
    }

    public String getLastHeader() {
        if (this.surveyFinisher == null) {
            return null;
        }
        return this.surveyFinisher.getLastHeader();
    }

    public String getLastChartPath() {
        if (this.surveyFinisher == null) {
            return null;
        }
        return this.surveyFinisher.getLastChartPath();
    }

    public String getLastSummary() {
        if (this.surveyFinisher == null) {
            return null;
        }
        return this.surveyFinisher.getLastSummary();
    }
}
