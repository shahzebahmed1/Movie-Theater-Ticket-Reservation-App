public class PaymentController {
    private FinancialInstitution financialInstitution;

    public PaymentController(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public boolean processPayment(String cardNumber, String cvv, String expiryDate, String cardHolderName, double amount) {
        boolean isValid = financialInstitution.validateCard(cardNumber, cvv, expiryDate, cardHolderName);
    
        if (!isValid) {
            System.out.println("Card validation failed. Payment rejected.");
            return false;
        }

        System.out.println("Processing payment of $" + amount + " for card: " + cardNumber);
        return true; 
    }

    public void addCardToFinancialInstitution(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryDate, cardHolderName);
        financialInstitution.addCard(paymentInfo);
    }
}
