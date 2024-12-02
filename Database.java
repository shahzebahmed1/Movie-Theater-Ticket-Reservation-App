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
    static final String DATABASE = "jdbc:mysql://localhost:3306/acmeplex"; // should be consistent if you use the query given

    public Database(String user, String password) {
        this.USER = user;
        this.PASSWORD = password;
    }
    
    public static void main(String[] args) throws SQLException {
        Database db = new Database("root", "password"); // remember to change credentials
        //uncomment the following method u wanna test

    }
    
    // method to get movies from database
    public void getMovies() throws SQLException {
    	
    	// establish connection to database
    	try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
    		
    		// query to get all titles from movie table
    		String query = "SELECT title FROM movies";
    		
    		// get the result of this query
    		try (PreparedStatement ps = connection.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
    			
    			// print out all the titles to terminal
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
    
    // method to insert card into database
    public void insertCard(String cardNumber, String cvv, String expiryDate, String cardHolderName) throws SQLException {
    	
    	// establish connection to database
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to insert into payment info table
            String query = "INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder, username) VALUES (?, ?, ?, ?, ?)";
            
            // fill in query with the variables
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, cardNumber);
            preparedStatement.setString(2, cvv);
            preparedStatement.setDate(3, java.sql.Date.valueOf(expiryDate));
            preparedStatement.setString(4, cardHolderName);
            preparedStatement.setNull(5, java.sql.Types.VARCHAR); // set username as null for now
            
            // check if any rows are changed, and print to terminal what happens
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

    // method to validate card info
    public boolean validateCard(PaymentInfo paymentInfo) throws SQLException {
    	
    	// establish connection to database
        try(Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to see if the payment info exists in our table
            String query = "SELECT COUNT(*) FROM paymentInfo WHERE cardNumber = ? AND cvv = ? AND expireDate = ? AND cardHolder = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, paymentInfo.getCardNumber());
            preparedStatement.setString(2, paymentInfo.getCvv());
            preparedStatement.setDate(3, java.sql.Date.valueOf(paymentInfo.getExpiryDate()));
            preparedStatement.setString(4, paymentInfo.getCardHolderName());
            
            // get the result of this query
            ResultSet result = preparedStatement.executeQuery();
            
            // see if its valid
            if (result.next() && result.getInt(1) > 0) {
                System.out.println("valid card information");
                return true;
            }
        }catch(Exception e){
            System.out.println("something went wrong " + e );
        }
        return false;
    }

    // method to get all the movies from our database
    public ArrayList<Movie> getAllMovies(boolean availability) throws SQLException {
    	
    	// create an arraylist of movies to return
        ArrayList<Movie> movies = new ArrayList<>();
        
        // establish connection to database
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// boolean is if we want to get public vs not public movies
            String query = "SELECT * FROM movies";
            if (availability){
                query+= " WHERE availableToPublic = TRUE";
            }
            
            // get all of the data from the result set into the arraylist
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
    
    // method to remove movies from database
    public void removeMovie(int movieID) throws SQLException {
    	
    	// establish connection to database
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) 
        {
        	// disable foreign key checks
            String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 0;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(disableForeignKeyChecks)) {
                preparedStatement.executeUpdate();
            }
            
            // delete all tickets associated with the ID
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
            
            // delete all seats associated with the ID
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
            
            // delete all movies associated with the ID
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
            
            // re-enable the foreign key checks
            String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 1;";
            try (PreparedStatement preparedStatement = connection.prepareStatement(enableForeignKeyChecks)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
    }

    // method to add movie to database
    public void addMovie(String title, String genre, int duration, boolean availability, String showTime, double ticketPrice) {
    	
    	// queries to add movie, showtime and seat at the same time because theyre all related
        String movieQuery = "INSERT INTO movies (title, duration, genre, availableToPublic, preReleasedTicketsLeft, ticketPrice) VALUES (?, ?, ?, ?, ?, ?)";
        String showtimeQuery = "INSERT INTO showtimes (movieID, time) VALUES (?, ?)";
        String seatQuery = "INSERT INTO seats (availability, seat_row, seat_column, movieID) VALUES (?, ?, ?, ?)";
        
        // establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        	
        	 // create prepared statements
             PreparedStatement movieStatement = connection.prepareStatement(movieQuery, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement showtimeStatement = connection.prepareStatement(showtimeQuery);
             PreparedStatement seatStatement = connection.prepareStatement(seatQuery)) {
        	
        	// set parameters for movie
            movieStatement.setString(1, title);
            movieStatement.setInt(2, duration);
            movieStatement.setString(3, genre);
            movieStatement.setBoolean(4, availability);
            movieStatement.setInt(5, 2);
            movieStatement.setDouble(6, ticketPrice);
            
            int rowsAffected = movieStatement.executeUpdate();
            
            // if movie is created properly move onto the showtimes and seats
            if (rowsAffected > 0) {
            	
                try (ResultSet generatedKeys = movieStatement.getGeneratedKeys()) {
                	
                    if (generatedKeys.next()) {
                        int movieID = generatedKeys.getInt(1);
                        
                        // set parameters for the showtime
                        showtimeStatement.setInt(1, movieID);
                        showtimeStatement.setString(2, showTime);
                        showtimeStatement.executeUpdate();
                        
                        // add 25 seats into our database
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
    
    
    // method to update a movie
    public void updateMovieInDatabase(Movie movie) throws SQLException{
    	
    	// establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to update how many tickets are left
            String query = "UPDATE movies SET preReleasedTicketsleft = ? WHERE movieID = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, movie.getPreReleasedTickets());
                preparedStatement.setInt(2, movie.getMovieId());
                
                // update the data and check if it updated properly
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
    
    // method to check if a ticket exists
    public boolean checkTicketExists(int ticketID) throws SQLException {
    	
    	// establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to see if the the ticket exists in our table
            String query = "SELECT COUNT(*) FROM tickets WHERE ticketID = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
            	
            	// see if its valid and return true if so
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

    // method to cancel a ticket
    public void cancelTicket(int ticketID) throws SQLException {
    	
    	// establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to delete ticket from table
            String query = "DELETE FROM tickets WHERE ticketID = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
            	
                ps.setInt(1, ticketID);
                ps.executeUpdate(); // execute query 
            }
        }
    }

    // query to check tickets associated with card number
    public boolean checkCardNumberForTicket(int ticketID, String cardNumber) throws SQLException {
    	
    	// establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query that sees if there is ticket that has ticketID and cardNumber we want
            String query = "SELECT COUNT(*) FROM tickets WHERE ticketID = ? AND cardNumber = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
            	
                ps.setInt(1, ticketID);
                ps.setString(2, cardNumber);
                
                // check if a row exists and return true if it does
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // method to get tickets from a user
    public ArrayList<Ticket> getUserTickets(String username) throws SQLException {
    	
        ArrayList<Ticket> tickets = new ArrayList<>(); // create arraylist to store tickets in
        
        // query to get all tickets from a user
        String query = "SELECT * FROM tickets WHERE username = ?";
        
        // establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        		
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            
            // iterate through the result set
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    int showtimeID = rs.getInt("showtimeID");
                    int seatID = rs.getInt("seatID");
                    
                    // retrieve movie, showtime, and seat details from their respective tables and add to our arraylist
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
    
    // method to get the movie by their ID
    private Movie getMovieById(int movieID) throws SQLException {
    	
    	// query to get movies based on the ID
        String query = "SELECT * FROM movies WHERE movieID = ?";
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        		
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, movieID);
            
            // if the movie exists, create a movie object for it and return it
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

    // method to get showtime from the movieID
    public Showtime getShowtimeByMovie(int movieId) {
    	
    	// query to get the showtimeID and time for the movie
        String query = "SELECT showtimeID, time FROM showtimes WHERE movieID = ?";
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        		
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, movieId);
            ResultSet result = statement.executeQuery();
            
            // if the showtime exists, create a showtime object and return it
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
    
    // method to get showtime from showtimeID
    public Showtime getShowtimeById(int showtimeID) throws SQLException {
    	
    	// query to get showtime from their ID
        String query = "SELECT * FROM showtimes WHERE showtimeID = ?";
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        		
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, showtimeID);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
            	// if the showtime exists, create a showtime object and return it
                if (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    String time = rs.getString("time");
                    return new Showtime(showtimeID, movieID, time);
                }
            }
        }
        return null;
    }
    
    // method to get a seat based on its ID
    public Seat getSeatById(int seatID) throws SQLException {
    	
    	// query to get seat based on its ID
        String query = "SELECT * FROM seats WHERE seatID = ?";
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
        		
            PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, seatID);
            
            try (ResultSet rs = ps.executeQuery()) {
            	
            	// if the seat exists, create a seat object and return it
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

    // method to get all the seats for a movie
    public ArrayList<Seat> getSeatsForMovie(int movieID) throws SQLException {
    	
        ArrayList<Seat> seats = new ArrayList<>(); // arraylist to store our seats
        
        // establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to get all seats from the movie ID
            String query = "SELECT * FROM seats WHERE movieID = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, movieID);
                
                // put all of the seats into the arraylist
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

    // method to update the availability of a seat
    public boolean updateSeatAvailability(int seatId, int availability) throws SQLException {
    	
    	// establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to change the availability of the seat 
            String query = "UPDATE seats SET availability = ? WHERE seatID = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, availability);
                ps.setInt(2, seatId);
                int rowsUpdated = ps.executeUpdate();
                System.out.println("Updated seat availability");
                
                // check if the seat is updated
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

    // method to get seat from a ticket
    public Seat getSeatForTicket(int ticketID) {  
    	
        Seat seat = null; // if the seat doesn't exist return null
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to get seat from ticket ID
            String query = "SELECT seatID FROM tickets WHERE ticketID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ticketID);
    
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // if seat exists use other function to create a seat object and return it
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
    
    // method to get seat details
    public Seat getSeatDetails(int seatID) {
    	
        Seat seat = null; // if the seat doesn't exist return null
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            
        	// query to get seats from seatID
            String query = "SELECT * FROM seats WHERE seatID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, seatID);
    
            ResultSet result = preparedStatement.executeQuery();
            
            // if the seat exists create a seat object and return it
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
    
    // method to add ticket to database
    public void insertTicket(Ticket ticket, String username, String cardNumber, java.util.Date dateBought) throws SQLException {
    	
    	// establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to get time of the film
            String showtimeQuery = "SELECT time FROM showtimes WHERE showtimeID = ?";
            String showtime = null; // return null if time doesn't exist
            
            try (PreparedStatement showtimeStatement = connection.prepareStatement(showtimeQuery)) {
                showtimeStatement.setInt(1, ticket.getShowtime().getShowtimeID());
                try (ResultSet rs = showtimeStatement.executeQuery()) {
                    if (rs.next()) {
                        showtime = rs.getString("time"); // update the time if it exists
                    }
                }
            }
            
            // query to insert a ticket
            String query = "INSERT INTO tickets (movieID, showtimeID, seatID, username, cardNumber, dateBought, dateOfFilm) VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            // fill in parameters
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, ticket.getMovie().getMovieId());
            preparedStatement.setInt(2, ticket.getShowtime().getShowtimeID());
            preparedStatement.setInt(3, ticket.getSeat().getSeatID());
            if (username != null && !username.isEmpty()) {
                preparedStatement.setString(4, username);
            } else {
                preparedStatement.setNull(4, java.sql.Types.VARCHAR);
            }
            preparedStatement.setString(5, cardNumber);
            preparedStatement.setDate(6, new java.sql.Date(dateBought.getTime()));
            preparedStatement.setString(7, showtime);
            
            // check if ticket is successfully added
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

    // method to get all usernames from database
    public ArrayList<String> getAllUsernames() throws SQLException {
    	
        ArrayList<String> usernames = new ArrayList<>(); // arraylist to store all usernames
        
        // establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to get usernames from user
            String query = "SELECT username FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
            		
                 ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    usernames.add(result.getString("username")); // add to our arraylist
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e);
        }
        return usernames;
    }

    // method to delete a user from database
    public boolean deleteUserFromDatabase(String username) {
    	
    	// establish a conenction
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to delete a user from their username
            String query = "DELETE FROM users WHERE username = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                
                int rowsDeleted = preparedStatement.executeUpdate(); 
                System.out.println("Deleted user");
                return rowsDeleted > 0; // check if user is deleted
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
    
    // method to get payment info from a user
    public PaymentInfo getPaymentInfoForUser(String username) {
    	
        PaymentInfo paymentInfo = null; // assume null initially
        
        // query to get payment info of a user based on their name
        String query = "SELECT cardNumber, cvv, expireDate, cardHolder FROM paymentInfo WHERE username = ?";
        
        // establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            
            // if the payment info exists, create an object with it and return it
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
    
    // method to update the balance of a user
    public void updateUserBalance(String username, double amount) throws SQLException {
    	
    	// establish a connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
        	
        	// query to update the balance of a users credit card
            String query = "UPDATE paymentinfo SET balance = balance - ? WHERE username = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setDouble(1, amount);
                ps.setString(2, username);
                
                // check if balance is updated or not
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Balance updated for user: " + username + " by amount: " + amount); // Debug statement
                } else {
                    System.out.println("Failed to update balance for user: " + username); // Debug statement
                }
            }
        }
    }
    
    // method to get price of a ticket
    public double getTicketPrice(int movieId) throws SQLException {
    	
    	// query to get price of ticket based on movie ID
        String query = "SELECT ticketPrice FROM movies WHERE movieID = ?";
        
        // establish connection
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, movieId);
            
            // if there is a ticket price then return it
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("ticketPrice");
                }
            }
        }
        return 0.0;
    }

    public String getUsernameForCard(String cardNumber) throws SQLException {
        String query = "SELECT username FROM paymentInfo WHERE cardNumber = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cardNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        }
        return null;
    }

    public Ticket getTicketById(int ticketID) throws SQLException {
        String query = "SELECT * FROM tickets WHERE ticketID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, ticketID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int movieID = rs.getInt("movieID");
                    int showtimeID = rs.getInt("showtimeID");
                    int seatID = rs.getInt("seatID");
                    System.out.println("Retrieved ticket details: movieID=" + movieID + ", showtimeID=" + showtimeID + ", seatID=" + seatID);
                    
                    Movie movie = getMovieById(movieID);
                    if (movie == null) {
                        System.out.println("Movie is null for movieID=" + movieID);
                    }
                    
                    Showtime showtime = getShowtimeById(showtimeID);
                    if (showtime == null) {
                        System.out.println("Showtime is null for showtimeID=" + showtimeID);
                    }
                    
                    Seat seat = getSeatById(seatID);
                    if (seat == null) {
                        System.out.println("Seat is null for seatID=" + seatID);
                    }
                    
                    if (movie != null && showtime != null && seat != null) {
                        System.out.println("Successfully retrieved ticket components.");
                        return new Ticket(movie, showtime, seat);
                    } else {
                        System.out.println("One of the ticket components is null. Movie: " + movie + ", Showtime: " + showtime + ", Seat: " + seat);
                    }
                } else {
                    System.out.println("No ticket found for ticketID=" + ticketID);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving ticket by ID: " + e);
        }
        return null;
    }

    public GiftCard getGiftCardById(int giftCardID) throws SQLException {
        String query = "SELECT * FROM giftCards WHERE giftCardID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, giftCardID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double balance = rs.getDouble("giftCardBalance");
                    String expireDate = rs.getString("expireDate");
                    return new GiftCard(giftCardID, balance, expireDate);
                }
            }
        }
        return null;
    }

    public void deleteGiftCardById(int giftCardID) throws SQLException {
        String query = "DELETE FROM giftCards WHERE giftCardID = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, giftCardID);
            ps.executeUpdate();
        }
    }

    public void setAnnualFeePaid(String username, boolean isPaid) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE, USER, PASSWORD)) {
            String query = "UPDATE users SET isAnnualFeePaid = ? WHERE username = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setBoolean(1, isPaid);
                ps.setString(2, username);
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Annual fee status updated for user: " + username);
                } else {
                    System.out.println("Failed to update annual fee status for user: " + username);
                }
            }
        }
    }
}