public class RegisteredUser extends User {
    private String name;
    private String address;
    private PaymentInfo paymentInfo;

    public RegisteredUser(String name, String address, PaymentInfo paymentInfo) {
        this.name = name;
        this.address = address;
        this.paymentInfo = paymentInfo;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }
}
