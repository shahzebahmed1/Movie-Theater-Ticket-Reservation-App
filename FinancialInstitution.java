public class FinancialInstitution {
    private Database database;

    public FinancialInstitution(Database database) {
        this.database = database;
    }

    public void addCard(PaymentInfo paymentInfo) {
        database.insertCard(paymentInfo.getCardNumber(), paymentInfo.getCvv(), paymentInfo.getExpiryDate(), paymentInfo.getCardHolderName());
        System.out.println("Card added to the database.");
    }

    public boolean validateCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        return database.validateCard(cardNumber, cvv, expiryDate, cardHolderName);
    }


}
