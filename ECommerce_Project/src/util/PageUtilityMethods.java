package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PageUtilityMethods
{

    /**
     * Loads image into BufferedImage private class variable
     * @param url -- client's listing url image
     * @return
     */
    public static BufferedImage loadImage(String url) {
        final int RESIZE_WIDTH = 150;
        final int RESIZE_HEIGHT = 200;

        BufferedImage image = null;
        BufferedImage resizedImage = null;

        try {
            image = ImageIO.read(new URL(url));
        } catch (IOException ex) { }

        resizedImage = resizeImage(image, RESIZE_WIDTH, RESIZE_HEIGHT);
        return resizedImage;
    }

    private static BufferedImage resizeImage(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public static JPanel createLogoPanel() {
        JPanel imagePanel = new JPanel();
        BufferedImage logo = null;
        final String path = "https://farm2.staticflickr.com/1410/1385703004_0c7b798b98.jpg";

        try {
            logo = ImageIO.read(new URL(path));
        } catch (IOException ex) {
        }

        assert logo != null;
        imagePanel.add(new JLabel(new ImageIcon(logo)));

        return imagePanel;
    }
}
