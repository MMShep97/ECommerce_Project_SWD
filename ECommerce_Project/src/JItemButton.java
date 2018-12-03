import javax.swing.*;

public class JItemButton extends JButton {

    private Item item;

    public JItemButton(String name, Item item){
        super(name);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
