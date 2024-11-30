import java.sql.SQLException;
import java.util.ArrayList;

public class MovieController {

    private Database database;

    public MovieController(Database database) {
        this.database = database;
    }

    public ArrayList<Movie> browseMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            movies = database.getAllMovies();
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

}
