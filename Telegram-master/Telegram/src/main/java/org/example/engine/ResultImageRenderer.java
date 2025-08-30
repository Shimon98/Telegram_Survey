
package org.example.engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ResultImageRenderer {
    public static String renderSummaryImage(String summary) {
        if (summary == null) summary = "";
        BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0,0,800,600);
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 16));
            int x = 20, y = 40, lineH = 22;
            for (String line : summary.split("\n")) {
                g.drawString(line, x, y);
                y += lineH;
                if (y > 560) break;
            }
        } finally {
            g.dispose();
        }
        try {
            File f = File.createTempFile("survey_results_", ".png");
            ImageIO.write(img, "png", f);
            return f.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}
