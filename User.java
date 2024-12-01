import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class User {
	
	private ArrayList<Ticket> tickets;
    private String username;
	
    public static void main(String[] args) throws SQLException {
    	
        Database db = new Database("root", "password"); // remember to change credentials
        User u = new User();
        u.register(db, "pogman", "poggers", "Poggeth", "Pog Dr.", "12345", "123", "2004-03-27");

    }
	
	

    public User() {
        this.tickets = new ArrayList<>();
    }
	
	public void cancelTicket() {
		
		// TODO
	};
	
	public boolean login(Database db, String username, String password) throws SQLException {
		
		// create a connection
		try (Connection connection = DriverManager.getConnection(db.DATABASE, db.USER, db.PASSWORD)) {
            
            // query to get username and password from users table
            String query = "SELECT username, password FROM users";
            
            // put the result of the query in a result set
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            	
            	// iterate through result set to get username and password, and see if it matches
            	while (rs.next()) {
            		
            		String databaseUser = rs.getString("username");
            		String databasePass = rs.getString("password");
            		
            		if (databaseUser.equals(username) && databasePass.equals(password)) {
            			System.out.println("login successful");
            			return true; // return true if theres a match
            		}
            		
    			}

            }
		}
		
		System.out.println("login not successful");
		
		return false;
		
	};
	
	public boolean register(Database db, String username, String password, String name, String address, String cardNumber, String cvv, String expireDate) {
		
		// create a connection
		try (Connection connection = DriverManager.getConnection(db.DATABASE, db.USER, db.PASSWORD)) {
            
            // get all usernames
            String query = "SELECT username FROM users";
            
            // put result of query in a result set
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            	
            	// check if username is already taken
            	while (rs.next()) {
            		
    				if (rs.getString("username").equals(username)) {
    					System.out.println("username taken");
    					return false;
    				}
    			}

            }
            
            query = "SELECT cardNumber FROM paymentinfo";
            
         // put result of query in a result set
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            	
            	// check if card number is already taken
            	while (rs.next()) {
            		
    				if (rs.getString("cardNumber").equals(cardNumber)) {
    					System.out.println("card number taken");
    					return false;
    				}
    			}

            }
            
            
        	
        	// query to insert user into database
            query = "INSERT INTO users (username, password, name, address, isAnnualFeePaid, annualFeeDate) " +
                           "VALUES (?, ?, ?, ?, ?, ?)";
            
            // create a prepared statement to put data into database
            try (PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, username);
                ps.setString(2, password); 
                ps.setString(3, name);
                ps.setString(4, address);
                ps.setBoolean(5, false); 
                
                // want the annual fee date to be a year from today
                LocalDate todaysDate = LocalDate.now();
                LocalDate nextYearsDate = todaysDate.plus(1, ChronoUnit.YEARS);
                ps.setDate(6, java.sql.Date.valueOf(nextYearsDate)); 
                
                // execute the change
                ps.executeUpdate();
                
                System.out.println("user succesfully added!");
                
            }
            
            // query to insert payment information of user into database
            query = "INSERT INTO paymentinfo (cardNumber, cvv, expireDate, cardHolder, username, balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {

                ps.setString(1, cardNumber);
                ps.setString(2, cvv); 
                ps.setDate(3, java.sql.Date.valueOf(expireDate));
                ps.setString(4, name);
                ps.setString(5, username);
                ps.setDouble(6, 0);
                
                // execute the change
                ps.executeUpdate();
                
                System.out.println("card succesfully added!");
                
            }
            
            
            
        } catch (Exception e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
		
		return true;
	};

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
	
}
