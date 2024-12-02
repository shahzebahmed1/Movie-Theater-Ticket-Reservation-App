public class RegisteredUser extends User {
    private String name;
    private String address;

    public RegisteredUser(String name, String address, PaymentInfo paymentInfo) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    
}
