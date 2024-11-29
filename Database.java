import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private final String USER;
    private final String PASSWORD;
    private static final String DATABASE = "jdbc:mysql://localhost:3306/acmeplex";

    public Database(String user, String password) {
        this.USER = user;
        this.PASSWORD = password;
    }
    
    public static void main(String[] args) throws SQLException {
        Database db = new Database("root", "password"); // remember to change credentials
        //uncomment the following method u wanna test
        //db.getMovies();
        //db.getAllMovies();
        //db.getAllUsers();
        //db.getAllSeats();
        //db.getAllShowtimes();
        //db.getAllTickets();
    }
    
    public void getMovies() throws SQLException {
    	
    	try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
    		
    		String query = "SELECT title FROM movies";
    		
    		try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
    			
    			while (rs.next()) {
    				String title = rs.getString("title");
    				System.out.println(title);
    			}
    			
    		} catch (Exception e) {
    			System.out.println("something went wrong" + e);
    		}
    		
    	} catch (Exception e) {
    		System.out.println("something went wrong" + e);
    	}
    	
    }

    public void insertCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        try(Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)){
            String query = "INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cvv);
            preparedStatement.setDate(3, java.sql.Date.valueOf(expiryDate));
            preparedStatement.setString(4, cardHolderName);
            int rowsChanged = preparedStatement.executeUpdate();
            if(rowsChanged>0){
                System.out.println("Payment information added successfully.");
            } else {
                System.out.println("Failed to add payment information.");
            }
        }catch(Exception e){
            System.out.print("something went wrong " + e);

        }
    }

    public boolean validateCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) {
        try(Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)){
            String query = "SELECT COUNT(*) FROM paymentInfo WHERE cardNumber = ? && cvv = ? && expiryDate = ? && cardHolder = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cvv);
            preparedStatement.setDate(3, java.sql.Date.valueOf(expiryDate));
            preparedStatement.setString(4, cardHolderName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return true;
            }
        }catch(Exception e){
            System.out.println("something went wrong " + e );
        }
        return false;
    }


    public void getAllMovies() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM movies";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    String title = rs.getString("title");
                    int duration = rs.getInt("duration");
                    String genre = rs.getString("genre");
                    System.out.println("Movie ID: " + movieID + ", Title: " + title + ", Duration: " + duration + " mins, Genre: " + genre);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
    }

    public void getAllUsers() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM users";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int userID = rs.getInt("userID");
                    String name = rs.getString("name");
                    String address = rs.getString("address");
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    System.out.println("User ID: " + userID + ", Name: " + name + ", Address: " + address + ", Username: " + username + ", Password: " + password);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
    }

    public void getAllSeats() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM seats";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int seatID = rs.getInt("seatID");
                    boolean availability = rs.getBoolean("availability");
                    int row = rs.getInt("seat_row");
                    String column = rs.getString("seat_column");
                    int movieID = rs.getInt("movieID");
                    System.out.println("Seat ID: " + seatID + ", Availability: " + availability + ", Row: " + row + ", Column: " + column + ", Movie ID: " + movieID);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
    }

    public void getAllShowtimes() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM showtimes";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int showtimeID = rs.getInt("showtimeID");
                    int movieID = rs.getInt("movieID");
                    String time = rs.getString("time");
                    System.out.println("Showtime ID: " + showtimeID + ", Movie ID: " + movieID + ", Time: " + time);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
    }

    public void getAllTickets() throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM tickets";
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int seatID = rs.getInt("seatID");
                    int showtimeID = rs.getInt("showtimeID");
                    int movieID = rs.getInt("movieID");
                    int userID = rs.getInt("userID");
                    System.out.println("Seat ID: " + seatID + ", Showtime ID: " + showtimeID + ", Movie ID: " + movieID + ", User ID: " + userID);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
    }
}