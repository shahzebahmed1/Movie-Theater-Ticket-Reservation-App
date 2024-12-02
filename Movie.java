import java.util.ArrayList;

public class Movie {
    private int movieId;
    private String title;
    private String genre;
    private int duration; //assume minutes for now
    private boolean availableToPublic;
    private int preReleasedTickets;
    private ArrayList<Showtime> showtimes;
    private double ticketPrice;

    public Movie(int movieId, String title, String genre, int duration, boolean availableToPublic, int preReleasedTickets) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.availableToPublic = availableToPublic;
        this.preReleasedTickets = preReleasedTickets;
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

    public boolean isAvailableToPublic() {
        return availableToPublic;
    }

    public void setAvailableToPublic(boolean availableToPublic) {
        this.availableToPublic = availableToPublic;
    }

    public int getPreReleasedTickets() {
        return preReleasedTickets;
    }

    public void setPreReleasedTickets(int preReleasedTickets) {
        this.preReleasedTickets = preReleasedTickets;
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

    public boolean canBookTicket(boolean isGuest) {
        if (!availableToPublic && isGuest) {
            return false; 
        }
        return preReleasedTickets > 0;
    }

    public void bookTicket() {
        if (preReleasedTickets > 0) {
            preReleasedTickets--;
        }
    }

    public double getTicketPrice() {
        return ticketPrice;
    }

    @Override
    public String toString() {
        return "Movie [ID: " + movieId + ", Title: " + title + ", Genre: " + genre + ", Duration: " + duration + " mins, Showtimes: " + showtimes + ", Available to public: " + availableToPublic + ", Pre-release tickets available: " + preReleasedTickets + "]";
    }
}
