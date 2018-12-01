import javax.swing.*;

public class Daniel_test {
    public static void main(String[] args){
        JFrame frame = new JFrame("O B E Y");
        PageLogin pl = new PageLogin();
        frame.add(pl);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1080, 720);
        frame.setVisible(true);
    }
}
