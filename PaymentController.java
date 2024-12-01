public class PaymentController {
    private FinancialInstitution financialInstitution;

    public PaymentController(FinancialInstitution financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public boolean processPayment(PaymentInfo paymentInfo, double amount) {
        boolean isValid = financialInstitution.validateCard(paymentInfo);
    
        if (!isValid) {
            System.out.println("Payment failed");
            return false;
        }

        System.out.println("Payment successful");
        return true; 
    }

    public void addCardToFinancialInstitution(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryDate, cardHolderName);
        financialInstitution.addCard(paymentInfo);
    }
}
