public class FinancialInstitution{
    private int userId;
    private String cardNumber;
    private String expiryDate;
    private int cvv;

    public FinancialInstitution(int userId, String cardNumber, String expiryDate, int cvv){
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public int getUserId(){
        return this.userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getCardNumber(){
        return this.cardNumber;
    }

    public void setCardNumber(String cardNumber){
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate(){
        return this.expiryDate;
    }

    public void setExpiryDate(String expiryDate){
        this.expiryDate = expiryDate;
    }

    public int getCvv(){
        return this.cvv;
    }

    public void setCvv(int cvv){
        this.cvv = cvv;
    }
}