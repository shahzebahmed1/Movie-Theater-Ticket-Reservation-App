import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class GiftCard {
	
	private int giftCardID;
	private double giftCardBalance;
	private String expireDate;	
	
	public void createCard(Database db, int giftCardID, double giftCardBalance) {
		
		// create a connection
		try (Connection connection = DriverManager.getConnection(db.DATABASE, db.USER, db.PASSWORD)) {
            
        	// query to insert giftcard into database
            String query = "INSERT INTO giftCards (giftCardID, giftCardBalance, expireDate) " +
                           "VALUES (?, ?, ?)";
            
            // create a prepared statement to put data into database
            try (PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setInt(1, giftCardID);
                ps.setDouble(2, giftCardBalance); 
                
                // want expire date to be a year from now
                LocalDate todaysDate = LocalDate.now();
                LocalDate nextYearsDate = todaysDate.plus(1, ChronoUnit.YEARS);
                ps.setDate(3, java.sql.Date.valueOf(nextYearsDate)); 
                
                // execute the change
                ps.executeUpdate();
                
                System.out.println("gift card succesfully added!");
                
            }        
            
            
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
		
	};
	
	
	public GiftCard() {
	}
	
	public GiftCard(int giftCardID, double giftCardBalance, String expireDate) {
	    this.giftCardID = giftCardID;
	    this.giftCardBalance = giftCardBalance;
	    this.expireDate = expireDate;
	}

	public int getGiftCardID() {
		return giftCardID;
	}
	public void setGiftCardID(int giftCardID) {
		this.giftCardID = giftCardID;
	}
	public double getGiftCardBalance() {
		return giftCardBalance;
	}
	public void setGiftCardBalance(double giftCardBalance) {
		this.giftCardBalance = giftCardBalance;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

}
