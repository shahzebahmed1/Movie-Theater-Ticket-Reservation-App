public class PaymentController {
    private FinancialInstitution financialInstitution;

    public PaymentController(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public boolean processPayment(String cardNumber, String cvv, String expiryDate, String cardHolderName, double amount) {
        boolean isValid = financialInstitution.validateCard(cardNumber, cvv, expiryDate);

        if (!isValid) {
            return false;
        }

        boolean paymentSuccess = financialInstitution.processPayment(cardNumber, amount);
        if (paymentSuccess) {
            System.out.println("Payment completed successfully!");
            return true;
        } else {
            System.out.println("Payment failed. Please try again.");
            return false;
        }
    }

    public void addCardToFinancialInstitution(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryDate, cardHolderName);
        financialInstitution.addCard(paymentInfo);
    }
}
