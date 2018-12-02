import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

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

    public void addFunds(double credits)
    {
        this.credit += credits;
    }

    public boolean checkPassword(String password)
    {
        return this.password.equals(password);
    }

    public boolean makePurchase(double price)
    {
        if(credit - price < 0.0) return false;

        this.credit-=price;
        return true;
    }

    public String toCSVFormat()
    {
        return getUsername()+","+this.password+","+getCredit()+"\n";
    }

    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(username);
        out.writeObject(password);
        out.writeObject(credit);

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        username = (String) in.readObject();
        password = (String) in.readObject();
        credit = (double) in.readObject();
    }
}
