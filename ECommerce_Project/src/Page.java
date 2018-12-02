import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public interface Page {

    final JPanel navbar = new JPanel();
    final JButton homeButton = new JButton("HOME");
    final JButton browseButton = new JButton("BROWSE");
    final JButton loginButton = new JButton("LOGIN/SIGNUP");

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
