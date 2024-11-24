import java.util.ArrayList;

public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private int duration; //assume minutes for now
    private ArrayList<Showtime> showtimes;

    public Movie(int movieId, String title, String genre, int duration) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.showtimes = new ArrayList<>();
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getDuration() {
        return duration;
    }

    public ArrayList<Showtime> getShowtimes() { //like the checkAvailability() in UML?
        return showtimes;
    }

    public void addShowtime(Showtime showtime) {
        this.showtimes.add(showtime);
    }

    public void removeShowtime(Showtime showtime) {
        this.showtimes.remove(showtime);
    }

    @Override
    public String toString() {
        return "Movie [ID: " + movieId + ", Title: " + title + ", Genre: " + genre + ", Duration: " + duration + " mins, Showtimes: " + showtimes + "]";
    }
}
