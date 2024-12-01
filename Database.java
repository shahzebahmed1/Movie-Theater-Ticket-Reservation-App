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
    

    public void insertCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder, username) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cvv);
            preparedStatement.setDate(3, java.sql.Date.valueOf(expiryDate));
            preparedStatement.setString(4, cardHolderName);
            preparedStatement.setNull(5, java.sql.Types.VARCHAR); // Set username as null
            int rowsChanged = preparedStatement.executeUpdate();
            if (rowsChanged > 0) {
                System.out.println("Payment information added successfully.");
            } else {
                System.out.println("Failed to add payment information.");
            }
        } catch (Exception e) {
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(query); ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    int movieID = result.getInt("movieID");
                    String title = result.getString("title");
                    int duration = result.getInt("duration");
                    String genre = result.getString("genre");
                    boolean availableToPublic = result.getBoolean("availableToPublic");
                    int preReleasedTickets = result.getInt("preReleasedTicketsleft");
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

    public void removeMovie(int movieID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String disableFKChecks = "SET FOREIGN_KEY_CHECKS = 0;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(disableFKChecks)) {
                preparedStatement.executeUpdate();
            }
    
            String deleteTicketsQuery = "DELETE FROM tickets WHERE movieID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteTicketsQuery)) {
                preparedStatement.setInt(1, movieID);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("tickets removed.");
                } else {
                    System.out.println("Tickets not found");
                }
            }
    
            String deleteSeatsQuery = "DELETE FROM seats WHERE movieID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSeatsQuery)) {
                preparedStatement.setInt(1, movieID);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("seats removed");
                } else {
                    System.out.println("Seats not found");
                }
            }
    
            String deleteMovieQuery = "DELETE FROM movies WHERE movieID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteMovieQuery)) {
                preparedStatement.setInt(1, movieID);
                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Movie deleted");
                } else {
                    System.out.println("Movie not found");
                }
            }
    
            String enableFKChecks = "SET FOREIGN_KEY_CHECKS = 1;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(enableFKChecks)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    public void addMovie(String title, String genre, int duration, boolean availability, String showTime) {
        String movieQuery = "INSERT INTO movies (title, duration, genre, availableToPublic, preReleasedTicketsLeft) VALUES (?, ?, ?, ?, ?)";
        String showtimeQuery = "INSERT INTO showtimes (movieID, time) VALUES (?, ?)";
        String seatQuery = "INSERT INTO seats (availability, seat_row, seat_column, movieID) VALUES (?, ?, ?, ?)";
        
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement movieStatement = connection.prepareStatement(movieQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement showtimeStatement = connection.prepareStatement(showtimeQuery);
             PreparedStatement seatStatement = connection.prepareStatement(seatQuery)) {
    
            movieStatement.setString(1, title);
            movieStatement.setInt(2, duration);
            movieStatement.setString(3, genre);
            movieStatement.setBoolean(4, availability);
            movieStatement.setInt(5, 2);
    
            int rowsAffected = movieStatement.executeUpdate();
    
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = movieStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int movieID = generatedKeys.getInt(1);
    
                        showtimeStatement.setInt(1, movieID);
                        showtimeStatement.setString(2, showTime);
                        showtimeStatement.executeUpdate();
    
                        for (int row = 1; row <= 5; row++) {
                            for (char column = 'A'; column <= 'E'; column++) {
                                seatStatement.setBoolean(1, true);
                                seatStatement.setInt(2, row);
                                seatStatement.setString(3, String.valueOf(column));
                                seatStatement.setInt(4, movieID);
                                seatStatement.addBatch();
                            }
                        }
    
                        seatStatement.executeBatch();
                        System.out.println("Movie, showtime and seats added");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }
    
    
    
    

    public void updateMovieInDatabase(Movie movie) throws SQLException{
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "UPDATE movies SET preReleasedTicketsleft = ? WHERE movieID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, movie.getPreReleasedTickets());
                preparedStatement.setInt(2, movie.getMovieId());
                int rowsUpdated = preparedStatement.executeUpdate();
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

    public Showtime getShowtimeByMovie(int movieId) {
        String query = "SELECT showtimeID, time FROM showtimes WHERE movieID = ?";

        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, movieId);
            ResultSet result = statement.executeQuery();
            
            if (result.next()) {
                int showtimeId = result.getInt("showtimeID");
                String time = result.getString("time");
                return new Showtime(showtimeId, movieId, time);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    
    public ArrayList<String> getAllUsernames() throws SQLException {
        ArrayList<String> usernames = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "SELECT username FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    usernames.add(result.getString("username"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return usernames;
    }

    public boolean deleteUserFromDatabase(String username) {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                int rowsDeleted = preparedStatement.executeUpdate();
                System.out.println("Deleted user");
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public PaymentInfo getPaymentInfoForUser(String username) {
        PaymentInfo paymentInfo = null;
        String query = "SELECT cardNumber, cvv, expireDate, cardHolder FROM paymentInfo WHERE username = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String cardNumber = rs.getString("cardNumber");
                    String cvv = rs.getString("cvv");
                    String expireDate = rs.getString("expireDate");
                    String cardHolder = rs.getString("cardHolder");
                    paymentInfo = new PaymentInfo(cardNumber, cvv, expireDate, cardHolder);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving payment info: " + e);
        }
        return paymentInfo;
    }
    
    
}