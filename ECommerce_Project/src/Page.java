import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public interface Page {

    JPanel navbar = new JPanel();
    JButton homeButton = new JButton("HOME");
    JButton browseButton = new JButton("BROWSE");
    JButton loginButton = new JButton("LOGIN/SIGNUP");

    static JPanel createNavbar() {

        //change color of buttons
        homeButton.setBackground(Color.BLACK);
        browseButton.setBackground(Color.BLACK);
        loginButton.setBackground(Color.BLACK);
        homeButton.setForeground(Color.WHITE);
        browseButton.setForeground(Color.WHITE);
        loginButton.setForeground(Color.WHITE);

        navbar.setLayout(new GridLayout(1, 1));
        navbar.add(homeButton);
        navbar.add(browseButton);
        navbar.add(loginButton);
        navbar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return navbar;
    }

    static BufferedImage loadImage(String url) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new URL(url));
        } catch (IOException ex) {
        }
        return image;
    }

    static JPanel createLogoPanel() {
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
