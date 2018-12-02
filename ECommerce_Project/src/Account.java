public class Account
{
    private final String username;
    private final String password;
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
}
