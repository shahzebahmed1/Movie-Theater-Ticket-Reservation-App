public class PaymentInfo {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String cardHolderName;

    public PaymentInfo(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }
}
