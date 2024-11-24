public class Seat {
    private String availability; //either Available or Booked
    private int row;
    private int column;

    public Seat(int row, int column) {
        this.availability = "Available";
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

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Seat [Row: " + row + ", Column: " + column + ", Availability: " + availability + "]";
    }
}
