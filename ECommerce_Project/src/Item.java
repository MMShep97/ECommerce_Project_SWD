import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Item implements Serializable
{
    private int listingID;
    private String name;
    private double price;
    private String seller;
    private String description;
    private String imageURL;

    public Item(int listingID, String name, double price, String seller, String description, String imageURL)
    {
        this.listingID = listingID;
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.description = description;
        this.imageURL = imageURL;
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(listingID);
        out.writeObject(name);
        out.writeObject(price);
        out.writeObject(seller);
        out.writeObject(description);
        out.writeObject(imageURL);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        listingID = (int) in.readObject();
        name = (String) in.readObject();
        price = (double) in.readObject();
        seller = (String) in.readObject();
        description = (String) in.readObject();
        imageURL = (String) in.readObject();
    }

    @Override
    public int hashCode() { return listingID; }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Item)
        {
            if(listingID == ((Item) o).listingID  && name.equals(((Item) o).name) && price == ((Item) o).price &&
                seller.equals(((Item) o).seller) && description.equals(((Item) o).description) &&
                imageURL.equals(((Item) o).imageURL))
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
