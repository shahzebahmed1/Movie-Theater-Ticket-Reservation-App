public class Showtime {
    private String time; //must decide format

    public Showtime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    @Override
    public String toString() {
        return "Showtime [Time: " + time + "]";
    }
}
