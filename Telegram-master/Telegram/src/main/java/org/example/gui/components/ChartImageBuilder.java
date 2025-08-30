package org.example.gui.components;

import org.example.engine.SurveyResult;
import org.example.engine.SurveyState;
import org.example.model.OptionForQuestion;
import org.example.model.Question;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ChartImageBuilder {
    private static final int MAX_W = 820;
    private static final int MAX_H = 520;
    private static final int CANVAS_SIDE_PAD = 20;
    private static final int TOP_PAD = 10;
    private static final int HEADER_H = 22;
    private static final int BETWEEN_QUESTIONS_GAP = 12;
    private static final int BASE_CHART_W = 680;
    private static final int BASE_CHART_H_TOTAL = 360;
    private static final int MIN_CHART_H = 100;
    private static final int BAR_BOTTOM_PAD = 28;
    private static final int BAR_TOP_PAD = 8;
    private static final int BAR_COL_GAP = 10;
    private static final int LABEL_OFFSET_Y = 14;
    private static final int VALUE_OFFSET_Y = 4;
    private static final Color COLOR_BG = Color.WHITE;
    private static final Color COLOR_AXES = Color.BLACK;
    private static final Color COLOR_BAR = new Color(250, 246, 2, 255);
    private static final String FONT_FAMILY = "Dialog";
    private static final int FONT_TITLE_STYLE = Font.BOLD;
    private static final int FONT_TITLE_SIZE = 13;
    private static final int FONT_LABEL_STYLE = Font.PLAIN;
    private static final int FONT_LABEL_SIZE = 12;
    private static final String TMP_PREFIX = "survey-charts-";
    private static final String TMP_SUFFIX = ".png";
    private static final String IMAGE_FORMAT = "png";


    public static String buildCombinedImage(SurveyState state, SurveyResult result) {
        try {
            List<Question> qs = state.getCurrentSurvey().getQuestions();
            int qCount = qs.size();

            int chartW = BASE_CHART_W;
            int chartH = calcChartHeightPerQuestion(qCount);

            int width = calcCanvasWidth(chartW);
            int height = calcCanvasHeight(qCount, chartH);

            BufferedImage base = createCanvas(width, height);
            Graphics2D g = base.createGraphics();
            try {
                applyHints(g);
                paintBackground(g, width, height);
                drawAllQuestions(g, qs, result, chartW, chartH);
            } finally {
                g.dispose();
            }

            BufferedImage finalImg = scaleIfNeeded(base, width, height, MAX_W, MAX_H);
            return writeTempPng(finalImg);
        } catch (Exception e) {
            return null;
        }
    }



    private static int calcChartHeightPerQuestion(int qCount) {
        int h = BASE_CHART_H_TOTAL / Math.max(1, qCount);
        return Math.max(MIN_CHART_H, h);
    }

    private static int calcCanvasWidth(int chartW) {
        return chartW + (CANVAS_SIDE_PAD * 2);
    }

    private static int calcCanvasHeight(int qCount, int chartH) {
        return qCount * (chartH + HEADER_H + BETWEEN_QUESTIONS_GAP) + TOP_PAD + BETWEEN_QUESTIONS_GAP;
    }

    private static BufferedImage createCanvas(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    private static void applyHints(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private static void paintBackground(Graphics2D g, int width, int height) {
        g.setColor(COLOR_BG);
        g.fillRect(0, 0, width, height);
        g.setColor(COLOR_AXES);
    }

    private static void drawAllQuestions(Graphics2D g, List<Question> qs,
                                         SurveyResult result, int chartW, int chartH) {
        int y = TOP_PAD;
        for (int qi = 0; qi < qs.size(); qi++) {
            y = drawSingleQuestion(g, qs.get(qi), qi, result, chartW, chartH, y);
            y += BETWEEN_QUESTIONS_GAP;
        }
    }

    private static int drawSingleQuestion(Graphics2D g, Question q, int qi, SurveyResult result,
                                          int chartW, int chartH, int startY) {
        setFontTitle(g);
        g.drawString("Q" + (qi + 1) + ": " + q.getText(), CANVAS_SIDE_PAD, startY + 16);

        int y = startY + HEADER_H;

        List<OptionForQuestion> opts = q.getOptions();
        int n = opts.size();

        int[] counts = safeCounts(result.getOptionCountsForQuestion(qi), n);
        int max = maxCount(counts);

        int barW = computeBarWidth(chartW, n);
        int baseY = y + chartH - BAR_BOTTOM_PAD;
        int usableH = chartH - BAR_BOTTOM_PAD - BAR_TOP_PAD;

        int x = CANVAS_SIDE_PAD;
        for (int i = 0; i < n; i++) {
            int v = counts[i];
            int bh = (max == 0 ? 0 : (int) Math.round((v * 1.0 / max) * usableH));
            int by = baseY - bh;

            drawBar(g, x, by, barW, bh);
            drawValueAboveBar(g, x, by, barW, v);
            drawLabelUnderBar(g, x, baseY, barW, opts.get(i).getText());

            x += barW + BAR_COL_GAP;
        }
        return y + chartH;
    }

    private static void setFontTitle(Graphics2D g) {
        g.setFont(new Font(FONT_FAMILY, FONT_TITLE_STYLE, FONT_TITLE_SIZE));
    }

    private static void setFontLabel(Graphics2D g) {
        g.setFont(new Font(FONT_FAMILY, FONT_LABEL_STYLE, FONT_LABEL_SIZE));
    }

    private static int[] safeCounts(int[] counts, int n) {
        if (counts == null) counts = new int[0];
        int[] out = new int[n];
        for (int i = 0; i < n; i++) out[i] = (i < counts.length ? counts[i] : 0);
        return out;
    }

    private static int maxCount(int[] a) {
        int max = 0;
        for (int v : a) if (v > max) max = v;
        return Math.max(1, max);
    }

    private static int computeBarWidth(int chartW, int n) {
        return Math.max(1, (chartW - (n + 1) * BAR_COL_GAP) / Math.max(1, n));
    }

    private static void drawBar(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(COLOR_BAR);
        g.fillRect(x, y, w, h);
        g.setColor(COLOR_AXES);
        g.drawRect(x, y, w, h);
    }

    private static void drawLabelUnderBar(Graphics2D g, int x, int baseY, int barW, String text) {
        setFontLabel(g);
        String t = fitTextToWidth(g, text, barW);
        int tw = g.getFontMetrics().stringWidth(t);
        g.drawString(t, x + Math.max(0, (barW - tw) / 2), baseY + LABEL_OFFSET_Y);
    }

    private static void drawValueAboveBar(Graphics2D g, int x, int barTopY, int barW, int value) {
        setFontLabel(g);
        String s = String.valueOf(value);
        int tw = g.getFontMetrics().stringWidth(s);
        g.drawString(s, x + Math.max(0, (barW - tw) / 2), barTopY - VALUE_OFFSET_Y);
    }

    private static String fitTextToWidth(Graphics2D g, String text, int maxWidth) {
        if (text == null) return "";
        FontMetrics fm = g.getFontMetrics();
        if (fm.stringWidth(text) <= maxWidth) return text;

        String ell = "...";
        int ellW = fm.stringWidth(ell);
        int limit = Math.max(0, maxWidth - ellW);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            sb.append(text.charAt(i));
            if (fm.stringWidth(sb.toString()) > limit) {
                break;
            }
        }
        return sb.length() == 0 ? ell : sb.toString() + ell;
    }

    private static BufferedImage scaleIfNeeded(BufferedImage src, int width, int height, int maxW, int maxH) {
        double sx = (width  > maxW) ? (maxW * 1.0 / width)  : 1.0;
        double sy = (height > maxH) ? (maxH * 1.0 / height) : 1.0;
        double s  = Math.min(1.0, Math.min(sx, sy));
        if (s >= 1.0) return src;

        int nw = Math.max(1, (int) Math.round(width  * s));
        int nh = Math.max(1, (int) Math.round(height * s));
        BufferedImage scaled = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaled.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.drawImage(src, 0, 0, nw, nh, null);
        } finally {
            g.dispose();
        }
        return scaled;
    }

    private static String writeTempPng(BufferedImage img) {
        try {
            File out = File.createTempFile(TMP_PREFIX, TMP_SUFFIX);
            ImageIO.write(img, IMAGE_FORMAT, out);
            return out.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}
