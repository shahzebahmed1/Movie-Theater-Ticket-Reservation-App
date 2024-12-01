public class Seat {
    private String availability; //either Available or Booked
    private int row;
    private char column;
    private int seatId;


    public Seat(int seatId, int row, char column, String availability) {
        this.seatId = seatId;
        this.availability = availability;
        this.row = row;
        this.column = column;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public int getRow() {
        return row;
    }

    public char getColumn() {
        return column;
    }

    public int getSeatId(){
        return this.seatId;
    }

    @Override
    public String toString() {
        return "Seat [Row: " + row + ", Column: " + column + ", Availability: " + availability + "]";
    }
}
