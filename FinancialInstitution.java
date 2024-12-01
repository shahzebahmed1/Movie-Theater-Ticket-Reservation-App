import java.sql.SQLException;

public class FinancialInstitution {
    private Database database;

    public FinancialInstitution(Database database) {
        this.database = database;
    }

    public void addCard(PaymentInfo paymentInfo) {
        try{
            database.insertCard(paymentInfo.getCardNumber(), paymentInfo.getCvv(), paymentInfo.getExpiryDate(), paymentInfo.getCardHolderName());

        }catch(SQLException e){
            System.out.println("Error occured while adding new card "+ e);
        }
        System.out.println("Card added to the database.");
    }

    public boolean validateCard(PaymentInfo paymentInfo) {
        boolean result = false;
        try{
            result = database.validateCard(paymentInfo);
        }catch(SQLException e){
            System.out.println("Could not validate payment information: " + e);
        }
        return result;
    }


}
