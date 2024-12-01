import java.sql.SQLException;
import java.util.ArrayList;

public class MovieController {

    private Database database;

    public MovieController(Database database) {
        this.database = database;
    }

    public ArrayList<Movie> browseMovies(boolean availableToPublic) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            movies = database.getAllMovies(availableToPublic);
        } catch (SQLException e) {
            System.out.println("Error fetching movies: " + e.getMessage());
        }
        return movies;
    }

    // Search for movies by name
    public ArrayList<String> searchMovie(String query, ArrayList<String> movies) {
        ArrayList<String> results = new ArrayList<>();
        for (String movie : movies) {
            if (movie.toLowerCase().contains(query.toLowerCase())) {
                results.add(movie);
            }
        }
        return results;
    }

    
    public boolean bookTicketForMovie(int movieId, boolean isGuest) {
        ArrayList<Movie> movies = browseMovies(isGuest);
    
        for (Movie movie : movies) {
            if (movie.getMovieId() == movieId) {
                if (movie.canBookTicket(isGuest)) {
                    movie.bookTicket();
                    try {
                        database.updateMovieInDatabase(movie); 
                    } catch (SQLException e) {
                        System.out.println("Error updating pre-release tickets" + e);
                        return false;
                    }
                    return true; 
                } else {
                    System.out.println("Cannot book ticket");
                    return false; 
                }
            }
        }
    
        System.out.println("Movie not found.");
        return false;
    }
    

}
