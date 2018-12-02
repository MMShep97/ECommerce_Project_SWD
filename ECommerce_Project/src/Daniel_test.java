import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Daniel_test {
    public static void main(String[] args) {
        JFrame frame = new JFrame("O B E Y");
        PageLogin pl = new PageLogin();
        frame.add(pl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }
}
