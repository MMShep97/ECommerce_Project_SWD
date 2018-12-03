import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Account holds a users username, password, and credits
 */
public class Account implements Serializable
{
    private String username;
    private String password;
    private double credit;

    public Account(String username, String password)
    {
        this.username = username;
        this.password = password;
        this.credit = 0.0;
    }

    public String getUsername()
    {
        return username;
    }

    public double getCredit()
    {
        return credit;
    }

    /**
     * Adds input amount of credits to the account
     * @param credits
     */
    public void addFunds(double credits) { this.credit += credits; }

    /**
     * Checks if the attempted password matches stored value
     * @param password
     * @return
     */
    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }

    /**
     * Attempts to make purchase, returns true if successful, false if not (insufficient funds)
     * @param price
     * @return
     */
    public boolean makePurchase(double price)
    {
        if(credit - price < 0.0) return false;

        this.credit-=price;
        return true;
    }

    /**
     * Parses the Account members into a comma separated String to be passed into a csv
     * @return
     */
    public String toCSVFormat()
    {
        return getUsername()+","+this.password+","+getCredit()+"\n";
    }

    /**
     * Allows Account to be sent over ObjectOutputStream, defines order of sending (Serializable)
     * @param out
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(credit);

    }

    /**
     * Allows Account to be received over ObjectInputStream, defines order of reception (Serializable)
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        username = (String) in.readObject();
        password = (String) in.readObject();
        credit = (double) in.readObject();
    }
}
