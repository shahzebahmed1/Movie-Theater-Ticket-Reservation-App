public class Seat {
    private int seatID;
    private boolean availability;
    private int row;
    private char column;
    private int movieID;

    public Seat(int seatID, boolean availability, int row, char column, int movieID) {
        this.seatID = seatID;
        this.availability = availability;
        this.row = row;
        this.column = column;
        this.movieID = movieID;
    }

    public int getSeatID() {
        return seatID;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    public int getMovieID() {
        return movieID;

    }

    @Override
    public String toString() {
        return "Seat [ID: " + seatID + ", Availability: " + availability + ", Row: " + row + ", Column: " + column + ", Movie ID: " + movieID + "]";
    }
}
