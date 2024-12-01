import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Database {

    final String USER;
    final String PASSWORD;
    static final String DATABASE = "jdbc:mysql://localhost:3306/acmeplex";

    public Database(String user, String password) {
        this.USER = user;
        this.PASSWORD = password;
    }
    
    public static void main(String[] args) throws SQLException {
        Database db = new Database("root", "password"); // remember to change credentials
        //uncomment the following method u wanna test
        // db.getMovies();
        // db.getAllMovies();
        // db.getAllUsers();
        // db.getAllSeats();
        // db.getAllShowtimes();
        // db.getAllTickets();
        // db.insertCard("1234567812345679", "333", "2025-12-31", "Bob Doe");
        // db.validateCard("1234567812345679", "333", "2025-12-31", "Bob Doe");

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
    

    public void insertCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) throws SQLException{
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

    public boolean validateCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) throws SQLException {
        try(Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)){
            String query = "SELECT COUNT(*) FROM paymentInfo WHERE cardNumber = ? AND cvv = ? AND expireDate = ? AND cardHolder = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cvv);
            preparedStatement.setDate(3, java.sql.Date.valueOf(expiryDate));
            preparedStatement.setString(4, cardHolderName);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("valid card information");
                return true;
            }
        }catch(Exception e){
            System.out.println("something went wrong " + e );
        }
        return false;
    }


    public ArrayList<Movie> getAllMovies(boolean availability) throws SQLException {
        ArrayList<Movie> movies = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM movies";
            if (availability){
                query+= " WHERE availableToPublic = TRUE";
            }
            try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    String title = rs.getString("title");
                    int duration = rs.getInt("duration");
                    String genre = rs.getString("genre");
                    boolean availableToPublic = rs.getBoolean("availableToPublic");
                    int preReleasedTickets = rs.getInt("preReleasedTicketsleft");
                    System.out.println("Movie ID: " + movieID + ", Title: " + title + ", Duration: " + duration + " mins, Genre: " + genre);
                    Movie movie = new Movie(movieID, title, genre, duration, availableToPublic, preReleasedTickets);
                    movies.add(movie);
                }
            } catch (Exception e) {
                System.out.println("something went wrong: " + e);
            }
        } catch (Exception e) {
            System.out.println("something went wrong: " + e);
        }
        return movies;

    }


    public void updateMovieInDatabase(Movie movie) throws SQLException{
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "UPDATE movies SET preReleasedTicketsleft = ? WHERE movieID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movie.getPreReleasedTickets());
                ps.setInt(2, movie.getMovieId());
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Update pre-release ticket number for movie");
                } else {
                    System.out.println("Could not update pre-release ticket number");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error" + e);
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

    public boolean checkTicketExists(int ticketID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT COUNT(*) FROM tickets WHERE ticketID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, ticketID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void cancelTicket(int ticketID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "DELETE FROM tickets WHERE ticketID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, ticketID);
                ps.executeUpdate();
            }
        }
    }
}