public class FinancialInstitution {
    private Database database; 

    public FinancialInstitution(Database database) {
        this.database = database;
    }

    public void addCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        database.insertCard(cardNumber, cvv, expiryDate, cardHolderName);
        System.out.println("Card added to the database.");
    }

    public boolean validateCard(String cardNumber, String cvv, String expiryDate) {
        return database.validateCard(cardNumber, cvv, expiryDate);
    }

    public boolean processPayment(String cardNumber, double amount) {
        if (database.cardExists(cardNumber)) {
            System.out.println("Processing payment of $" + amount + " for card: " + cardNumber);
            return true;
        } else {
            System.out.println("Card not found. Payment failed.");
            return false;
        }
    }
}
