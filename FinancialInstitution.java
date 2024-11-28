import java.util.HashMap;
import java.util.Map;

public class FinancialInstitution {
    private Map<String, PaymentInfo> cardDatabase = new HashMap<>();

    public void addCard(PaymentInfo paymentInfo) {
        cardDatabase.put(paymentInfo.getCardNumber(), paymentInfo);
    }

    public boolean validateCard(String cardNumber, String cvv, String expiryDate) {
        PaymentInfo card = cardDatabase.get(cardNumber);
        if (card != null) {
            return card.getCvv().equals(cvv) && card.getExpiryDate().equals(expiryDate);
        }
        return false;
    }

    public boolean processPayment(String cardNumber, double amount) {
        if (cardDatabase.containsKey(cardNumber)) {
            return true;
        }
        return false;
    }
}
