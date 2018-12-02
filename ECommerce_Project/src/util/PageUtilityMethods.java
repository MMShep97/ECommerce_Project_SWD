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
        final String path = "http://www.mkyong.com/image/mypic.jpg";

        try {
            logo = ImageIO.read(new URL(path));
        } catch (IOException ex) {
        }

        assert logo != null;
        imagePanel.add(new JLabel(new ImageIcon(logo)));

        return imagePanel;
    }
}
