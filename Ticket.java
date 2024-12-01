public class Ticket {
    private Movie movie;
    private Showtime showtime;
    private Seat seat;

    public Ticket(Movie movie, Showtime showtime, Seat seat) {
        this.movie = movie;
        this.showtime = showtime;
        this.seat = seat;
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
}
