import java.util.ArrayList;

public class TheatreController {
    private ArrayList<Theatre> theatres;

    public TheatreController() {
        this.theatres = new ArrayList<>();
    }

    public ArrayList<Theatre> getTheatres() {
        return theatres;
    }

    public void addTheatre(Theatre theatre) {
        this.theatres.add(theatre);
    }

    public void removeTheatre(Theatre theatre) {
        this.theatres.remove(theatre);
    }
}
