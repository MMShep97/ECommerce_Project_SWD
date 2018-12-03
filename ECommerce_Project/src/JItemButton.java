import javax.swing.*;

/*
 * JItemButton extends JButton. It is simply a JButton that has an Item member and a getItem method.
 */
public class JItemButton extends JButton {

    /*
     * An item. This is useful to us when we want to connect certain buttons to items
     */
    private Item item;

    /*
     * constructor, sets the text by calling super and assign item
     */
    public JItemButton(String text, Item item){
        super(text);
        this.item = item;
    }

    //getter for item
    public Item getItem() {
        return item;
    }
}
