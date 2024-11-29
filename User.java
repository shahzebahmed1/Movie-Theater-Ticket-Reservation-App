import java.util.ArrayList;
import java.util.List;

public class User {
	
	private ArrayList<Ticket> tickets;

    public User() {
        this.tickets = new ArrayList<>();
    }
	
	public void cancelTicket() {
		
		// TODO
	};
	
	public void login() {
		
		// TODO
	};
	
	public void register() {
		
		// TODO
	};

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(Ticket ticket) {
        tickets.remove(ticket);
    }
	
}
