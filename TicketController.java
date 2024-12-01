import java.util.ArrayList;
import java.util.List;

public class TicketController {
    private List<Ticket> tickets; //should be database later. means the member functions will query the DB too

    public TicketController() {
        tickets = new ArrayList<>();
    }

    public Ticket bookTicket(User user, Movie movie, Showtime showtime, Seat seat) {
        if (!seat.getAvailability()) { // Check if the seat is available
            System.out.println("The seat is already booked.");
            return null;
        }

        Ticket ticket = new Ticket(movie, showtime, seat);
        ticket.issue();
        tickets.add(ticket);
        user.addTicket(ticket);
        return ticket;
    }

    public void cancelTicket(User user, Ticket ticket) {
        if (!tickets.contains(ticket)) {
            System.out.println("Ticket not found in the system.");
            return;
        }
        ticket.cancel();
        tickets.remove(ticket);
        user.removeTicket(ticket);
    }

    public List<Ticket> getTickets() {
        return tickets;
    }
}
