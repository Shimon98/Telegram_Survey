package org.example.gui;

import org.example.SurveyResult;
import org.example.SurveyState;
import org.example.model.OptionForQuestion;
import org.example.model.Question;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

/** Creates one tall PNG image with per-question bar charts. */
public class ChartImageBuilder {

    public static String buildCombinedImage(SurveyState state, SurveyResult result) {
        try {
            java.util.List<Question> qs = state.getCurrentSurvey().getQuestions();
            int qCount = qs.size();

            int chartW = 700;
            int chartH = 180;
            int headerH = 24;
            int gap = 14;
            int width = chartW + 40;
            int height = qCount * (chartH + headerH + gap) + 20;

            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE); g.fillRect(0,0,width,height);
            g.setColor(Color.BLACK);

            int y = 10;
            for (int qi = 0; qi < qCount; qi++) {
                Question q = qs.get(qi);
                g.setFont(new Font("Dialog", Font.BOLD, 14));
                g.drawString("Q" + (qi + 1) + ": " + q.getText(), 10, y + 16);
                y += headerH;

                java.util.List<OptionForQuestion> opts = q.getOptions();
                int n = opts.size();
                int[] counts = result.getOptionCountsForQuestion(qi);
                int max = 1;
                for (int c : counts) if (c > max) max = c;
                int barW = Math.max(1, (chartW - (n + 1) * 10) / n);
                int x = 20;
                int baseY = y + chartH - 30;
                int usableH = chartH - 40;

                for (int i=0;i<n;i++) {
                    int v = (i < counts.length ? counts[i] : 0);
                    int h = (int) Math.round((v * 1.0 / max) * usableH);
                    int by = baseY - h;
                    g.setColor(new Color(100, 140, 220, 220));
                    g.fillRect(x, by, barW, h);
                    g.setColor(Color.BLACK);
                    g.drawRect(x, by, barW, h);
                    String lbl = opts.get(i).getText();
                    g.setFont(new Font("Dialog", Font.PLAIN, 12));
                    int tw = g.getFontMetrics().stringWidth(lbl);
                    g.drawString(lbl, x + Math.max(0,(barW - tw)/2), baseY + 16);
                    String cv = String.valueOf(v);
                    int tv = g.getFontMetrics().stringWidth(cv);
                    g.drawString(cv, x + Math.max(0,(barW - tv)/2), by - 4);
                    x += barW + 10;
                }
                y += chartH + gap;
            }
            g.dispose();
            File out = File.createTempFile("survey-charts-", ".png");
            ImageIO.write(img, "png", out);
            return out.getAbsolutePath();
        } catch (Exception e) { return null; }
    }
}