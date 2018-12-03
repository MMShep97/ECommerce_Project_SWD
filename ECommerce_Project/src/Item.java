import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Holds listing information, created by a seller (<code>ECommerceClient</code>) in the list item page (<code>PageListItem</code>)
 */
public class Item implements Serializable
{
    private int listingID;
    private String name;
    private double price;
    private String seller;
    private String description;
    private String imageURL;
    private int quantity;

    /**
     * Brings in and stores all listing information
     * @param listingID -- key used within server's item map to retrieve/store items
     * @param name -- item name
     * @param price -- item price
     * @param seller -- name of seller
     * @param description -- item desc.
     * @param imageURL -- direct url path to desired image
     * @param initQuantity -- quantity selling
     */
    public Item(int listingID, String name, double price, String seller, String description, String imageURL, int initQuantity)
    {
        this.listingID = listingID;
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.description = description;
        this.imageURL = imageURL;
        this.quantity = initQuantity;
    }

    /**
     * Decides whether or not specified item is similar to another item instance (used for similar items to be displayed)
     * @param other -- item to be compared to
     * @return boolean true if similar ( > .75 or 75% similarity) otherwise false
     */
    public boolean isSimilarTo(Item other)
    {
        String [] descriptWords = other.description.toUpperCase().split(" ");
        String uppercaseDescript = description.toUpperCase();
        double similarityCounter = 0;

        for(String w : descriptWords)
        {
            if(uppercaseDescript.contains(w)) similarityCounter++;
        }

        return ((similarityCounter/((double)descriptWords.length)) > 0.75);
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(listingID);
        out.writeObject(name);
        out.writeObject(price);
        out.writeObject(seller);
        out.writeObject(description);
        out.writeObject(imageURL);
        out.writeObject(quantity);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        listingID = (int) in.readObject();
        name = (String) in.readObject();
        price = (double) in.readObject();
        seller = (String) in.readObject();
        description = (String) in.readObject();
        imageURL = (String) in.readObject();
        quantity = (int) in.readObject();
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

    /**
     * Brings in listingID and sets it to <code>listingID</code> private class variable
     * @param listingID
     */
    public void setListingID(int listingID)
    {
        this.listingID = listingID;
    }

    /**
     * Gets and returns listingID
     * @return
     */
    public int getListingID()
    {
        return listingID;
    }

    /**
     * Gets and returns name
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets and returns price
     * @return
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Gets and returns seller
     * @return
     */
    public String getSeller()
    {
        return seller;
    }

    /**
     * Gets and returns description
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Gets and returns image url
     * @return
     */
    public String getImageURL()
    {
        return imageURL;
    }

    /**
     * Gets and returns quantity (items in stock)
     * @return
     */
    public int getQuantity()
    {
        return quantity;
    }
    
    public boolean purchased(int num)
    {
        if(quantity - num >= 0)
        {
            quantity-=num;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String toString()
    {
        return "[" + getListingID() + "] " + getName() + ": " + getDescription();
    }

    public String toCSVFormat()
    {
        return getListingID()+","+getName()+","+getPrice()+","+getSeller()+","+getDescription()+","+getImageURL()+","+getQuantity() +"\n";
    }
}
