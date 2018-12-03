import javax.swing.*;

public class JItemButton extends JButton {

    private Item item;

    public JItemButton(String text, Item item){
        super(text);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
