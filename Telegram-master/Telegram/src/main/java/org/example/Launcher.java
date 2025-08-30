package org.example;

import org.example.bot.Bot;
import org.example.bot.TelegramGateway;
import org.example.community.Community;
import org.example.community.CommunityRegistry;
import org.example.community.CommunityService;
import org.example.core.EngineSurveySender;
import org.example.core.SurveySender;
import org.example.engine.SurveyEngine;
import org.example.engine.TelegramPollEngine;
import org.example.gui.AppFrame;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.swing.*;

public class Launcher {
    public static void start() {
        SwingUtilities.invokeLater(() -> {
            try {
                AppFrame frame = new AppFrame(null, null);
                frame.setVisible(true);
                new Thread(() -> {
                    try {
                        CommunityRegistry registry = new CommunityRegistry();
                        CommunityService service  = new CommunityService(registry);
                        Bot bot = new Bot(service, registry);
                        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
                        api.registerBot(bot);
                        TelegramGateway gateway = new TelegramGateway(bot);
                        Community community     = new Community(registry);
                        TelegramPollEngine pollEngine = new TelegramPollEngine(gateway);
                        SurveyEngine engine      = new SurveyEngine(pollEngine, community);
                        bot.setSurveyEngine(engine);
                        SurveySender sender      = new EngineSurveySender(engine);
                        SwingUtilities.invokeLater(() -> frame.attachEngineAndSender(sender, engine));
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtilities.invokeLater(() ->
                                JOptionPane.showMessageDialog(null, e.getMessage(),
                                        "Startup error", JOptionPane.ERROR_MESSAGE));
                    }
                }, "EngineBootstrap").start();

            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Startup error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
