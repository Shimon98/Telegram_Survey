package org.example.gui;

import org.example.core.SurveySender;
import org.example.gui.cards.*;
import org.example.model.Survey;
import org.example.util.SurveyBuiltListener;
import org.example.engine.SurveyEngine;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame implements SurveyBuiltListener {
    private static final String APP_TITLE = "Survey Builder";
    private static final int FRAME_WIDTH = 900;
    private static final int FRAME_HEIGHT = 700;
    private static final int H_GAP = 0;
    private static final int V_GAP = 0;
    private static final String BTN_HOME_TEXT = "Home";
    private static final String BTN_MANUAL_TEXT = "Create (Manual)";
    private static final String BTN_AI_TEXT = "Create (AI)";
    private static final String BTN_PREVIEW_TEXT = "Preview";
    private static final String BTN_SEND_TEXT = "Send";
    private static final String BTN_CLOSE_TEXT = "Close";
    private static final String AI_CLIENT_ID = "319028015";
    private static final String MSG_LOADING = "Loading engine...";
    private static final String MSG_PREPARE_SEND = "Preparing to send...";
    private CardLayout layout;
    private JPanel root;
    private WelcomeCard welcome;
    private ManualSurveyCard manual;
    private ChatGptSurveyCard aiCard;
    private PreviewCard preview;
    private ProgressCard progress;
    private ResultsCard results;
    private SurveySender sender;
    private SurveyEngine engine;
    private Survey lastSurvey;
    private JToolBar tb;
    private JButton goWelcome;
    private JButton goManual;
    private JButton goAI;
    private JButton goPreview;
    private JButton sendBtn;
    private JButton closeBtn;

    public AppFrame(SurveySender sender, SurveyEngine engine) {
        super(APP_TITLE);
        this.sender = sender;
        this.engine = engine;
        this.results = new ResultsCard();
        this.progress = new ProgressCard();
        this.preview  = new PreviewCard();
        this.welcome  = new WelcomeCard();
        this.layout   = new CardLayout();
        this.root     = new JPanel(this.layout);

        initFrameBasics();
        initCards();
        initToolbar();
        wireNavActions();
        wireSendAction();
        wireCloseAction();
        initRootLayout();
        if (this.sender == null || this.engine == null) {
            setToolbarEnabled(false);
            progress.setStatus(MSG_LOADING);
            showCard(AppConst.CARD_PROGRESS);
        } else {
            setToolbarEnabled(true);
            showWelcome();
        }
        validate();
        repaint();
    }

    public void attachEngineAndSender(SurveySender sender, SurveyEngine engine) {
        this.sender = sender;
        this.engine = engine;
        setToolbarEnabled(true);
        progress.setStatus("");
        showWelcome();
    }

    private void setToolbarEnabled(boolean enabled) {
        if (goWelcome != null) goWelcome.setEnabled(true);
        if (goManual  != null) goManual.setEnabled(true);
        if (goAI      != null) goAI.setEnabled(true);
        if (goPreview != null) goPreview.setEnabled(true);
        if (sendBtn   != null) sendBtn.setEnabled(enabled && lastSurvey != null);
        if (closeBtn  != null) closeBtn.setEnabled(enabled && lastSurvey != null);
    }

    private void initFrameBasics() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setLocationRelativeTo(null);
    }

    private void initCards() {
        this.manual = new ManualSurveyCard(this);
        this.aiCard = new ChatGptSurveyCard(this, AI_CLIENT_ID);
    }

    private void initToolbar() {
        tb = new JToolBar();
        goWelcome = new JButton(BTN_HOME_TEXT);
        goManual  = new JButton(BTN_MANUAL_TEXT);
        goAI      = new JButton(BTN_AI_TEXT);
        goPreview = new JButton(BTN_PREVIEW_TEXT);
        sendBtn   = new JButton(BTN_SEND_TEXT);
        closeBtn  = new JButton(BTN_CLOSE_TEXT);

        tb.add(goWelcome);
        tb.add(goManual);
        tb.add(goAI);
        tb.add(goPreview);
        tb.addSeparator();
        tb.add(sendBtn);
        tb.add(closeBtn);
    }

    private void wireNavActions() {
        goWelcome.addActionListener(e -> showCard(AppConst.CARD_WELCOME));
        goManual.addActionListener(e  -> showCard(AppConst.CARD_MANUAL));
        goAI.addActionListener(e      -> showCard(AppConst.CARD_AI));
        goPreview.addActionListener(e -> {
            if (lastSurvey != null) preview.bindSurvey(lastSurvey);
            showCard(AppConst.CARD_PREVIEW);
        });
    }

    private void wireSendAction() {
        sendBtn.addActionListener(e -> {
            if (lastSurvey == null || sender == null) return;
            showCard(AppConst.CARD_PROGRESS);

            int delayMinutes = (preview != null ? preview.getDelayMinutesSafe() : 0);
            if (delayMinutes > 0) {
                scheduleSendWithDelay(delayMinutes);
            } else {
                startSendingNow();
            }
        });
    }

    private void wireCloseAction() {
        closeBtn.addActionListener(e -> {
            if (lastSurvey == null || sender == null) return;
            showCard(AppConst.CARD_PROGRESS);
            new CloseSurveyThread(sender, lastSurvey.getId(), engine, progress, results, this).start();
        });
    }

    private void initRootLayout() {
        setLayout(new BorderLayout(H_GAP, V_GAP));
        add(tb, BorderLayout.NORTH);
        root.add(welcome, AppConst.CARD_WELCOME);
        root.add(manual,  AppConst.CARD_MANUAL);
        root.add(aiCard,  AppConst.CARD_AI);
        root.add(preview, AppConst.CARD_PREVIEW);
        root.add(progress,AppConst.CARD_PROGRESS);
        root.add(results, AppConst.CARD_RESULTS);
        add(root, BorderLayout.CENTER);
    }

    private void showWelcome() { showCard(AppConst.CARD_WELCOME); }

    private void scheduleSendWithDelay(int delayMinutes) {
        new CountdownThread(progress, true, delayMinutes * 60).start();
        new ThreadFatherProject("DelayBeforeSend") {
            @Override public void run() {
                sleepMs(delayMinutes * 60L * 1000L);
                SwingUtilities.invokeLater(() -> startSendingNow());
            }
        }.start();
    }

    protected void showCard(String name) { layout.show(root, name); }

    @Override
    public void onSurveyBuilt(Survey survey) {
        this.lastSurvey = survey;
        setToolbarEnabled(sender != null && engine != null);
        preview.bindSurvey(survey);
        showCard(AppConst.CARD_PREVIEW);
    }

    public void startSendingNow() {
        progress.setStatus(MSG_PREPARE_SEND);
        new SendSurveyThread(sender, lastSurvey, progress).start();

        int seconds = (int) (org.example.config.Config.getSurveyDurationMs() / 1000L);
        new CountdownThread(progress, false, seconds).start();

        if (engine != null) {
            new SurveyWatcherThread(engine, progress, results, this).start();
        }
    }
}
