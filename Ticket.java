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
        seat.setAvailability(false); // Set availability to false (Booked)
        System.out.println("Issued a ticket for the movie " + movie.getTitle() + " at " + showtime.getTime());
    }

    public void cancel() {
        seat.setAvailability(true); // Set availability to true (Available)
        System.out.println("Ticket canceled for the movie " + movie.getTitle() + " at " + showtime.getTime());
    }

    @Override
    public String toString() {
        return "Ticket [Movie: " + movie.getTitle() + ", Showtime: " + showtime.getTime() + ", Seat: " + seat + "]";
    }
}
