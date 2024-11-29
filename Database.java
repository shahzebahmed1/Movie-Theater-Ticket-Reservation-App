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
        db.getMovies();
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

}