import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;


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
        //db.getSeatsForMovie(1);
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

    public boolean validateCard(PaymentInfo paymentInfo) throws SQLException {
        try(Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)){
            String query = "SELECT COUNT(*) FROM paymentInfo WHERE cardNumber = ? AND cvv = ? AND expireDate = ? AND cardHolder = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, paymentInfo.getCardNumber());
            preparedStatement.setString(2, paymentInfo.getCvv());
            preparedStatement.setDate(3, java.sql.Date.valueOf(paymentInfo.getExpiryDate()));
            preparedStatement.setString(4, paymentInfo.getCardHolderName());

            ResultSet result = preparedStatement.executeQuery();

            if (result.next() && result.getInt(1) > 0) {
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

    public boolean checkCardNumberForTicket(int ticketID, String cardNumber) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT COUNT(*) FROM tickets WHERE ticketID = ? AND cardNumber = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, ticketID);
                ps.setString(2, cardNumber);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Ticket> getUserTickets(String username) throws SQLException {
        ArrayList<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    int showtimeID = rs.getInt("showtimeID");
                    int seatID = rs.getInt("seatID");
                    // Retrieve movie, showtime, and seat details from their respective tables
                    Movie movie = getMovieById(movieID);
                    Showtime showtime = getShowtimeById(showtimeID);
                    Seat seat = getSeatById(seatID);
                    if (movie != null && showtime != null && seat != null) {
                        tickets.add(new Ticket(movie, showtime, seat));
                    }
                }
            }
        }
        return tickets;
    }

    private Movie getMovieById(int movieID) throws SQLException {
        String query = "SELECT * FROM movies WHERE movieID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, movieID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    int duration = rs.getInt("duration");
                    String genre = rs.getString("genre");
                    boolean availableToPublic = rs.getBoolean("availableToPublic");
                    int preReleasedTickets = rs.getInt("preReleasedTicketsleft");
                    return new Movie(movieID, title, genre, duration, availableToPublic, preReleasedTickets);
                }
            }
        }
        return null;
    }

    private Showtime getShowtimeById(int showtimeID) throws SQLException {
        String query = "SELECT * FROM showtimes WHERE showtimeID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, showtimeID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    String time = rs.getString("time");
                    return new Showtime(showtimeID, movieID, time);
                }
            }
        }
        return null;
    }

    private Seat getSeatById(int seatID) throws SQLException {
        String query = "SELECT * FROM seats WHERE seatID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, seatID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean availability = rs.getBoolean("availability");
                    int row = rs.getInt("seat_row");
                    String columnStr = rs.getString("seat_column");
                    char column = columnStr != null && columnStr.length() > 0 ? columnStr.charAt(0) : ' ';
                    int movieID = rs.getInt("movieID");
                    return new Seat(seatID, availability, row, column, movieID);
                }
            }
        }
        return null;
    }

    public ArrayList<Seat> getSeatsForMovie(int movieID) throws SQLException {
        ArrayList<Seat> seats = new ArrayList<>();
        
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT * FROM seats WHERE movieID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieID);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int seatID = rs.getInt("seatID");
                        boolean availability = rs.getBoolean("availability");
                        int row = rs.getInt("seat_row");
                        String column = rs.getString("seat_column");
                        Seat seat = new Seat(seatID, availability, row, column.charAt(0), movieID); 
                        seats.add(seat);
                    }
                } catch (Exception e) {
                    System.out.println("Error " + e);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        System.out.println("success");
        return seats;
        
    }

    public boolean updateSeatAvailability(int seatId, int availability) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "UPDATE seats SET availability = ? WHERE seatID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, availability);
                ps.setInt(2, seatId);
                int rowsUpdated = ps.executeUpdate();
                System.out.println("Updated seat availability");
                return rowsUpdated > 0;
            } catch (SQLException e) {
                System.out.println("Error updating seat" + e);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error " + e);
            return false;
        }
    }

    public Seat getSeatForTicket(int ticketID) {        
        Seat seat = null;
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT seatID FROM tickets WHERE ticketID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ticketID);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            
            if (resultSet.next()) {
                int seatID = resultSet.getInt("seatID");
                seat = getSeatDetails(seatID);
            }
        } catch (SQLException e) {
            System.out.println("Error getting ticket seat " + e);
        }
        System.out.println("Successfully retrieved ticket seat");
        return seat;
    }

    public Seat getSeatDetails(int seatID) {
        Seat seat = null;
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            
            String query = "SELECT * FROM seats WHERE seatID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, seatID);
    
            ResultSet result = preparedStatement.executeQuery();
            
            if (result.next()) {
                int seatId = result.getInt("seatID");
                int row = result.getInt("seat_row");
                char col = result.getString("seat_column").charAt(0);
                boolean availability = result.getInt("availability") == 1;
                int movieId = result.getInt("movieID");
                seat = new Seat(seatId, availability, row, col, movieId);
            }
        } catch (SQLException e) {
            System.out.println("Error getting seat details" + e);
        }
        
        return seat;
    }
    
    public void insertTicket(Ticket ticket, String username, String cardNumber, java.util.Date dateBought) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "INSERT INTO tickets (movieID, showtimeID, seatID, username, cardNumber, dateBought) VALUES (?, ?, ?, ?, ?, ?)";
            
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            
            int movieID = ticket.getMovie().getMovieId();
            int showtimeID = 1; 
            int seatID = ticket.getSeat().getSeatID(); 
            preparedStatement.setInt(1, movieID);
            preparedStatement.setInt(2, showtimeID);
            preparedStatement.setInt(3, seatID);

            if (username != null && !username.isEmpty()) {
                preparedStatement.setString(4, username);  
            } else {
                preparedStatement.setNull(4, java.sql.Types.VARCHAR);  
            }
            preparedStatement.setString(5, cardNumber);  
            preparedStatement.setDate(6, new java.sql.Date(dateBought.getTime()));  

            int rowsChanged = preparedStatement.executeUpdate();
            if (rowsChanged > 0) {
                System.out.println("Ticket added to the database");
            } else {
                System.out.println("Failed to add ticket");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
    
    
    
    
    
}