public class Showtime {
    private int showtimeID;
    private int movieID;
    private String time;

    public Showtime(int showtimeID, int movieID, String time) {
        this.showtimeID = showtimeID;
        this.movieID = movieID;
        this.time = time;
    }

    public int getShowtimeID() {
        return showtimeID;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Showtime [ID: " + showtimeID + ", Movie ID: " + movieID + ", Time: " + time + "]";
    }
}
