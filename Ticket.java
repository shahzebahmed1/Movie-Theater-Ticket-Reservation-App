import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ticket {
    private Movie movie;
    private Showtime showtime;
    private Seat seat;

    	
    public static void main(String[] args) throws SQLException {
    	
        Database db = new Database("root", "password"); // remember to change credentials
        Ticket t = new Ticket();
        System.out.println(t.doesCardHaveUser(db, "1234512345123451"));

    }
    
    public Ticket(Movie movie, Showtime showtime, Seat seat) {
        this.movie = movie;
        this.showtime = showtime;
        this.seat = seat;
    }
    
    public Ticket() {
    	
    }

    public Movie getMovie() {
        return movie;
    }

    public Showtime getShowtime() {
        return showtime;
    }

    public Seat getSeat() {
        return seat;
    }

    public void issue() {
        System.out.println("Issued a ticket for the movie" + movie.getTitle() + " at " + showtime.getTime());
    }

    public void cancel() {
        System.out.println("Ticket canceled for the movie " + movie.getTitle() + " at " + showtime.getTime());
    }

    @Override
    public String toString() {
        return "Ticket [Movie: " + movie.getTitle() + ", Showtime: " + showtime.getTime() + ", Seat: " + seat + "]";
    }
    
    public boolean doesCardHaveUser(Database db, String cardNumber) throws SQLException {

        // create connection
        try (Connection connection = DriverManager.getConnection(db.DATABASE, db.USER, db.PASSWORD)) {

            // query to find username with cardNumber
            String query = "SELECT username FROM paymentInfo WHERE cardNumber = ?";

            // execute query
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, cardNumber);

                // get result
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // if username is not null or empty, return true

                        if (rs.getString("username") == null) {
                            return false;
                        } else {
                            return true;
                        }

                    }
                }
            }
        }

        return false;
    }
    
    
}
