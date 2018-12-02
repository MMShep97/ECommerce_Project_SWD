package util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class PageUtilityMethods
{
    public static BufferedImage loadImage(String url) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new URL(url));
        } catch (IOException ex) {
        }
        return image;
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
