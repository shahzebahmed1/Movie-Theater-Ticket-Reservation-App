import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashSet;
import java.time.LocalTime;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class ImageJFrame {
    private String userType = "guest"; //default user type is "guest"
    private boolean isAdmin = false; //keeps tracks if the current user is an admin
    private JButton logoutButton; //logout button (only shown when logged in)
    private JButton orderHistoryButton, invoiceLookupButton, adminControlsButton;
    private JFrame mainFrame;
    private JTextField searchField;
    private JButton searchButton;
    private JButton loginButton;
    private JButton createAccountButton;
    private User currentUser; //stores current user
    private JButton bookTicketButton;

    private ArrayList<String> movies = new ArrayList<>(); //placeholder for movies
    private ArrayList<String> users = new ArrayList<>(); //placeholder for users

    private Database database;
    private MovieController movieController;

    ImageJFrame() {
        //Add some default movies for testing
        movies.add("Movie 1 - 12:00 PM");
        movies.add("Movie 2 - 3:00 PM");
        movies.add("Movie 3 - 6:00 PM");

        //Add some default users
        users.add("user1");
        users.add("user2");

        //create JFrame
        mainFrame = new JFrame("AcmePlex");
        mainFrame.setLayout(new BorderLayout());

        //create a layered pane for overlaying the image and buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(875, 500));

        //create a JLabel with a scaled image
        ImageIcon originalIcon = new ImageIcon("AcmePlex1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(875, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(0, 0, 875, 500);
        layeredPane.add(imageLabel, Integer.valueOf(0)); 

        //create a JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); 
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBounds(0, 0, 875, 500); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        //create buttons
        bookTicketButton = new JButton("Book Ticket");
        orderHistoryButton = new JButton("Order History");
        orderHistoryButton.setVisible(false); 
        invoiceLookupButton = new JButton("Invoice Lookup");
        createAccountButton = new JButton("Register");
        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        logoutButton.setVisible(false); 
        adminControlsButton = new JButton("Admin Controls");
        adminControlsButton.setVisible(false); //visible only for admins

        //add buttons to the button panel
        buttonPanel.add(bookTicketButton, gbc);

        gbc.gridy++;
        buttonPanel.add(orderHistoryButton, gbc);

        gbc.gridy++;
        buttonPanel.add(invoiceLookupButton, gbc);

        gbc.gridy++;
        buttonPanel.add(loginButton, gbc);

        gbc.gridy++;
        buttonPanel.add(createAccountButton, gbc);

        gbc.gridy++;
        buttonPanel.add(logoutButton, gbc);

        gbc.gridy++;
        buttonPanel.add(adminControlsButton, gbc);

        //add the button panel to the layered pane
        layeredPane.add(buttonPanel, Integer.valueOf(1)); //add buttons at the foreground layer

        //initialize database and movie controller
        database = new Database("root", "password"); // use your credentials!
        movieController = new MovieController(database);

        //ActionListener for buttons
        bookTicketButton.addActionListener(e -> showMovieSelectionPage());
        createAccountButton.addActionListener(e -> showRegistrationForm());
        loginButton.addActionListener(e -> showLoginFrame(loginButton, createAccountButton));
        logoutButton.addActionListener(e -> logout());
        invoiceLookupButton.addActionListener(e -> showInvoiceLookupFrame());
        adminControlsButton.addActionListener(e -> showAdminControls());
        orderHistoryButton.addActionListener(e -> {
            if (userType.equals("user")) {
                showOrderHistory(currentUser); //ensure currentUser is passed correctly
            }
        });

        mainFrame.add(layeredPane, BorderLayout.CENTER);

        mainFrame.setSize(875, 500);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    //show order history for user
    private void showOrderHistory(User user) {
        JFrame orderHistoryFrame = new JFrame("Order History");
        JPanel orderHistoryPanel = new JPanel(new BorderLayout());

        JTextArea orderHistoryTextArea = new JTextArea();
        orderHistoryTextArea.setEditable(false);

        //retrieve all tickets and their information belonging to the user
        try {
            ArrayList<Ticket> tickets = database.getUserTickets(user.getUsername());
            if (tickets.isEmpty()) {
                orderHistoryTextArea.setText("No tickets found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (Ticket ticket : tickets) {
                    sb.append(ticket.toString()).append("\n");
                }
                orderHistoryTextArea.setText(sb.toString());
            }
        } catch (SQLException e) {
            orderHistoryTextArea.setText("Error retrieving order history.");
        }

        orderHistoryPanel.add(new JScrollPane(orderHistoryTextArea), BorderLayout.CENTER);
        orderHistoryFrame.add(orderHistoryPanel);
        orderHistoryFrame.setSize(400, 300);
        orderHistoryFrame.setVisible(true);
    }

    //returns the current user
    private User getCurrentUser() {
        return currentUser;
    }

    //User login
    private void showLoginFrame(JButton loginButton, JButton createAccountButton) {
        JFrame loginFrame = new JFrame("Login");
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginSubmitButton = new JButton("Login");

        //add input fields
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginSubmitButton);

        loginSubmitButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User u = new User();

            //admin login
            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(loginFrame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                userType = "admin";
                isAdmin = true;
                logoutButton.setVisible(true);
                adminControlsButton.setVisible(true);
                loginButton.setVisible(false);
                createAccountButton.setVisible(false);
                bookTicketButton.setVisible(false);
                loginFrame.dispose();
            } else
                //verify user information in databse and login user
                try {
                    if (u.login(database, username, password)) {
                        JOptionPane.showMessageDialog(loginFrame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        userType = "user";
                        currentUser = u; 
                        currentUser.setUsername(username); 
                        logoutButton.setVisible(true);
                        orderHistoryButton.setVisible(true);
                        loginButton.setVisible(false);
                        createAccountButton.setVisible(false);
                        bookTicketButton.setVisible(true);
                        loginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                }
        });

        loginFrame.add(loginPanel);
        loginFrame.setSize(400, 200);
        loginFrame.setVisible(true);
    }

    //Logout user
    private void logout() {
        userType = "guest";
        isAdmin = false;
        currentUser = null; //clear the current user
        JOptionPane.showMessageDialog(mainFrame, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        logoutButton.setVisible(false);
        orderHistoryButton.setVisible(false);
        adminControlsButton.setVisible(false);
        loginButton.setVisible(true);
        createAccountButton.setVisible(true);
        bookTicketButton.setVisible(true);

        mainFrame.repaint();
    }

    //Register user
    private void showRegistrationForm() {
        JFrame registerFrame = new JFrame("Register");
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        LocalDate oneYearFromToday = LocalDate.now().plusYears(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        String formattedDate = oneYearFromToday.format(formatter);

        JLabel alertLabel = new JLabel("<html><span style='color:red; font-weight:bold;'>Note: $20 Annual Fee Charge. Your registration will be valid till " + formattedDate + ".</span></html>");
        alertLabel.setHorizontalAlignment(SwingConstants.CENTER);
        alertLabel.setPreferredSize(new Dimension(400, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        registerPanel.add(alertLabel, gbc);

        //username field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        registerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        registerPanel.add(usernameField, gbc);

        //password field
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 3;
        JPasswordField passwordField = new JPasswordField(15);
        registerPanel.add(passwordField, gbc);

        //name field
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        registerPanel.add(nameField, gbc);

        //address field
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3;
        JTextField addressField = new JTextField(15);
        registerPanel.add(addressField, gbc);

        //card number field
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        JTextField cardNumberField = new JTextField(15);
        registerPanel.add(cardNumberField, gbc);

        //expiry Date (YYYY-MM-DD format)
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Expiry Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 3;
        JTextField expiryDateField = new JTextField(15);
        registerPanel.add(expiryDateField, gbc);

        //cvv field
        gbc.gridx = 0;
        gbc.gridy = 4;
        registerPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 1;
        JTextField cvvField = new JTextField(15);
        registerPanel.add(cvvField, gbc);

        //checkbox for confirming annual fee
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        JCheckBox confirmFeeCheckbox = new JCheckBox("I confirm the $20 annual fee charge.");
        registerPanel.add(confirmFeeCheckbox, gbc);

        //register button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        registerButton.setEnabled(false); // Initially disabled
        registerPanel.add(registerButton, gbc);

        //enable register button only when the checkbox is selected
        confirmFeeCheckbox.addActionListener(e -> {
            registerButton.setEnabled(confirmFeeCheckbox.isSelected());
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String address = addressField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            //validate inputs for registration
            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //check for a valid expiry date
            if (!validDate(expiryDate)) {
                JOptionPane.showMessageDialog(registerFrame, "Please enter a valid expiry date in yyyy-MM-dd format.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //validation for card number and CVV
            if (cardNumber.length() != 16 || cvv.length() != 3) {
                JOptionPane.showMessageDialog(registerFrame, "Invalid card details.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //register new user
            User u = new User();
            if (u.register(database, username, password, name, address, cardNumber, cvv, expiryDate)) {
                try {
                    database.updateUserBalance(username, 20.00); //take away 20 from their balance
                    database.setAnnualFeePaid(username, true); 
                    JOptionPane.showMessageDialog(registerFrame, "Registration successful! Your membership is valid till: " + formattedDate, "Success", JOptionPane.INFORMATION_MESSAGE);
                    registerFrame.dispose();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(registerFrame, "Error updating balance or setting annual fee status: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(registerFrame, "Registration not successful! Duplicate username or card number exists ", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        registerFrame.add(registerPanel);
        registerFrame.pack();
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setVisible(true);
    }

    // helper function to see if date is valid
    public boolean validDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // format the date should be
        try {
            LocalDate d = LocalDate.parse(date, formatter);
            return true; 
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    //Show Invoice Lookup Frame
    private void showInvoiceLookupFrame() {
        JFrame invoiceFrame = new JFrame("Invoice Lookup");

        JPanel invoicePanel = new JPanel(new GridLayout(5, 1, 10, 10)); 
        JLabel invoiceLabel = new JLabel("Enter Invoice (ticketID) Number:");
        JTextField invoiceField = new JTextField();
        JLabel cardNumberLabel = new JLabel("Confirm Card Number Used to Pay:");
        JTextField cardNumberField = new JTextField();
        JButton lookupButton = new JButton("Lookup");

        //Add input fields
        invoicePanel.add(invoiceLabel);
        invoicePanel.add(invoiceField);
        invoicePanel.add(cardNumberLabel);
        invoicePanel.add(cardNumberField);
        invoicePanel.add(lookupButton);

        //search for ticket based on ticketID and cardnumber used to pay for ticket
        lookupButton.addActionListener(e -> {
            String invoiceNumber = invoiceField.getText();
            String cardNumber = cardNumberField.getText();
            try {
                int ticketID = Integer.parseInt(invoiceNumber);
                boolean ticketFound = database.checkTicketExists(ticketID);
                if (ticketFound) {
                    boolean cardMatches = database.checkCardNumberForTicket(ticketID, cardNumber);
                    if (cardMatches) {
                        int option = JOptionPane.showConfirmDialog(invoiceFrame, "Invoice found! Do you want to cancel the ticket for a refund?", "Invoice", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            Ticket t = database.getTicketById(ticketID); 
                            if (t != null) {
                                System.out.println("Ticket retrieved: " + t);
                                if (t.getMovie() != null) {
                                    int movieID = t.getMovie().getMovieId();
                                    double ticketPrice = 0.0;
                                    try {
                                        ticketPrice = database.getTicketPrice(movieID);
                                    } catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(invoiceFrame, "Error retrieving ticket price: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                    
                                    //reset seat availability
                                    Seat seat = database.getSeatForTicket(ticketID); 
                                    seat.setAvailability(true);
                                    movieController.updateSeatAvailability(seat);
                                    database.cancelTicket(ticketID);
                                    
                                    if (!t.doesCardHaveUser(database, cardNumber)) {
                                        // create a random 5 digit number
                                        Random rand = new Random();
                                        int giftCardID = 10000 + rand.nextInt(90000);
                                        
                                        GiftCard g = new GiftCard();
                                        double giftCardBalance = ticketPrice * 0.85; //we only refund 85% if unregistered user
                                        g.createCard(database, giftCardID, giftCardBalance);
                                        
                                        JOptionPane.showMessageDialog(invoiceFrame, "Ticket canceled. Gift Card created with 85% value with ID: " + giftCardID, "Success", JOptionPane.INFORMATION_MESSAGE);
                                        invoiceFrame.dispose();
                                        
                                    } else {
                                        //increase balance on the user's card
                                        try {
                                            String username = database.getUsernameForCard(cardNumber);
                                            if (username != null) {
                                                database.updateUserBalance(username, -ticketPrice); // Refund increases balance
                                                JOptionPane.showMessageDialog(invoiceFrame, "Ticket canceled. Refund issued.", "Success", JOptionPane.INFORMATION_MESSAGE);
                                            }
                                        } catch (SQLException ex) {
                                            JOptionPane.showMessageDialog(invoiceFrame, "Error updating user balance: " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                        invoiceFrame.dispose();
                                    }
                                } else {
                                    System.out.println("Movie is null in the ticket.");
                                    JOptionPane.showMessageDialog(invoiceFrame, "Error retrieving ticket details.", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } else {
                                System.out.println("Ticket is null.");
                                JOptionPane.showMessageDialog(invoiceFrame, "Error retrieving ticket details.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(invoiceFrame, "Card number does not match the one used for this ticket.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(invoiceFrame, "Invalid Invoice Number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException | SQLException ex) {
                JOptionPane.showMessageDialog(invoiceFrame, "Error processing invoice number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        invoiceFrame.add(invoicePanel);
        invoiceFrame.setSize(400, 300);
        invoiceFrame.setVisible(true);
    }

    // Show Admin Controls
    private void showAdminControls() {
        JFrame adminFrame = new JFrame("Admin Controls");

        JPanel adminPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton addMovieButton = new JButton("Add Movie");
        JButton deleteMovieButton = new JButton("Delete Movie");        
        JButton deleteUserButton = new JButton("Delete User");

        //Add buttons
        adminPanel.add(addMovieButton);
        adminPanel.add(deleteMovieButton);
        adminPanel.add(deleteUserButton);

        addMovieButton.addActionListener(e -> addMovie());
        deleteMovieButton.addActionListener(e -> deleteMovie());
        deleteUserButton.addActionListener(e -> deleteUser());

        adminFrame.add(adminPanel);
        adminFrame.setSize(400, 300);
        adminFrame.setVisible(true);
    }

    // Add Movie
    private void addMovie() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));  

        //Create and add input fields
        JLabel titleLabel = new JLabel("Movie Title:");
        JTextField titleField = new JTextField();
        JLabel showtimeLabel = new JLabel("Showtime (YYYY-MM-DD HH:MM:SS):");
        JTextField showtimeField = new JTextField(); 
        JLabel availableToPublicLabel = new JLabel("Public Availability (yes/no):");
        JTextField availableToPublicField = new JTextField();
        JLabel durationLabel = new JLabel("Duration:");
        JTextField durationField = new JTextField(); 
        JLabel genreLabel = new JLabel("Genre:");
        JTextField genreField = new JTextField(); 
        JLabel priceLabel = new JLabel("Ticket Price:");
        JTextField priceField = new JTextField(); 
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(showtimeLabel);
        panel.add(showtimeField);
        panel.add(availableToPublicLabel);
        panel.add(availableToPublicField);
        panel.add(durationLabel);
        panel.add(durationField);
        panel.add(genreLabel);
        panel.add(genreField);
        panel.add(priceLabel);
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(mainFrame, panel, "Enter Movie Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String title = titleField.getText().trim();
            String showtime = showtimeField.getText().trim();
            String availableToPublicInput = availableToPublicField.getText().trim().toLowerCase();
            String genre = genreField.getText().trim();
            String durationInput = durationField.getText().trim();
            String priceInput = priceField.getText().trim();

            //Check if input values are correct and not empty
            if (!title.isEmpty() && isValidTime(showtime) && (availableToPublicInput.equals("yes") || availableToPublicInput.equals("no"))
                    && !genre.isEmpty() && !priceInput.isEmpty()) {
                
                try {
                    int duration = Integer.parseInt(durationInput); 
                    double ticketPrice = Double.parseDouble(priceInput);
                    if (duration > 0 && ticketPrice > 0) {
                        boolean availableToPublic = availableToPublicInput.equals("yes");  //changes "yes" or "no" to boolean
                        database.addMovie(title, genre, duration, availableToPublic, showtime, ticketPrice); //Add a new movie from the inputs
                        JOptionPane.showMessageDialog(mainFrame, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(mainFrame,"Duration and price must be positive", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(mainFrame,"Invalid duration or price", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame,"Invalid input","Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Function to check if the time given is in the correct format
    private boolean isValidTime(String time) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            LocalDateTime.parse(time, timeFormatter);
            return true;
        } catch (DateTimeParseException e) {
            return false; 
        }
    }


    // Delete Movie
    private void deleteMovie() {
        ArrayList<Movie> movies = movieController.browseMovies(false);
        if (movies.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "No movies to delete", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //retrieves all the movies from the database along with their showtimes
        String[] movieTitlesWithShowtimes = new String[movies.size()];
        for (int i = 0; i < movies.size(); i++) {
            Movie movie = movies.get(i);
            Showtime showtime = database.getShowtimeByMovie(movie.getMovieId());
            movieTitlesWithShowtimes[i] = movie.getTitle() + " - Showtime: " + showtime.getTime();
        }

        //Create dropdown menu to select a movie to delete
        String selectedMovieTitle = (String) JOptionPane.showInputDialog(mainFrame, "Select movie to delete", "Delete", JOptionPane.QUESTION_MESSAGE, null,movieTitlesWithShowtimes, movieTitlesWithShowtimes[0]);
        if (selectedMovieTitle != null) {
            Movie movieToRemove = null;
            

            for (Movie movie : movies) {
                String movieTitleWithShowtime = movie.getTitle() + " - Showtime: " + database.getShowtimeByMovie(movie.getMovieId()).getTime();
                if (movieTitleWithShowtime.equals(selectedMovieTitle)) {
                    movieToRemove = movie;
                    break;
                }
            }
            
            //remove selected movie and showtime from database along with their seats. 
            if (movieToRemove != null) {
                try {
                    database.removeMovie(movieToRemove.getMovieId());
                    JOptionPane.showMessageDialog(mainFrame, "Movie deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException e) {
                    System.err.println("Error: " + e);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "Movie not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }


    // Delete User
    private void deleteUser() {
        try {
            ArrayList<String> usernames = database.getAllUsernames();
        
            if (usernames.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No users to delete", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            //Dropdown menu to select a user to delete
            String selectedUser = (String) JOptionPane.showInputDialog(mainFrame, "Select a user to delete:", "Delete User",
            JOptionPane.QUESTION_MESSAGE, null, usernames.toArray(new String[0]), usernames.get(0));
            //Delete user from database
            if (selectedUser != null) {
                if (database.deleteUserFromDatabase(selectedUser)) {
                    JOptionPane.showMessageDialog(mainFrame, "User deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Error deleting user", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(mainFrame, "Error" + e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Movie Selection
    private void showMovieSelectionPage() {
        JFrame movieFrame = new JFrame("Select Movie");

        JPanel moviePanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        //Movies shown will depend on if you are a RU or just user
        ArrayList<Movie> allMovies = userType.equals("guest") ? movieController.browseMovies(true) : movieController.browseMovies(false);

        //Retrieves all movies with unique titles
        HashSet<String> uniqueTitles = new HashSet<>();
        ArrayList<Movie> uniqueMovies = new ArrayList<>();
        for (Movie movie : allMovies) {
            if (uniqueTitles.add(movie.getTitle())) {
                uniqueMovies.add(movie);
            }
        }

        ArrayList<String> movieTitles = new ArrayList<>();
        for (Movie movie : uniqueMovies) {
            movieTitles.add(movie.getTitle());
        }

        JList<String> movieList = new JList<>(movieTitles.toArray(new String[0]));
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Selecting a movie title, will take you to the showtime page for that movie
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            String selectedTitle = movieList.getSelectedValue();
            if (selectedTitle != null) {
                ArrayList<Movie> selectedMovies = new ArrayList<>();
                for (Movie movie : allMovies) {
                    if (movie.getTitle().equals(selectedTitle)) {
                        selectedMovies.add(movie);
                    }
                }

                if (!selectedMovies.isEmpty()) {
                    showShowtimeSelectionPage(selectedTitle, selectedMovies);
                    movieFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(movieFrame, "Error, could not find showtimes", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(movieFrame, "Select a movie", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //Search functionality, users can search based on partial word match or full word match
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                ArrayList<String> searchResults = new ArrayList<>();
                for (Movie movie : uniqueMovies) {
                    if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(movie.getTitle());
                    }
                }
                if (searchResults.isEmpty()) {
                    JOptionPane.showMessageDialog(movieFrame, "No movies found matching the query.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    movieList.setListData(searchResults.toArray(new String[0]));
                }
            } else {
                movieList.setListData(movieTitles.toArray(new String[0]));
            }
        });

        moviePanel.add(new JLabel("Select a Movie:"), BorderLayout.NORTH);
        moviePanel.add(searchPanel, BorderLayout.NORTH);
        moviePanel.add(new JScrollPane(movieList), BorderLayout.CENTER);
        moviePanel.add(nextButton, BorderLayout.SOUTH);

        movieFrame.add(moviePanel);
        movieFrame.setSize(400, 300);
        movieFrame.setVisible(true);
    }
        
    //Movie showtime page
    private void showShowtimeSelectionPage(String title, ArrayList<Movie> movies) {
        JFrame showtimeFrame = new JFrame("Select Showtime");
    
        JPanel showtimePanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(movies.size(), 1, 5, 5)); 

        //Get all showtimes for the movie that has been selected
        for (Movie movie : movies) {
            Showtime showtime = database.getShowtimeByMovie(movie.getMovieId());
    
            if (showtime != null) {
                JButton showtimeButton = new JButton(showtime.getTime());
                
                //Clicking on showtime will take user to seat selection page
                showtimeButton.addActionListener(e -> {
                    showSeatSelectionPage(movie, movie.getMovieId(), showtime.getShowtimeID());
                    showtimeFrame.dispose();  
                });
                buttonPanel.add(showtimeButton);
            }
        }
    
        showtimePanel.add(new JLabel("Select a Showtime for " + title), BorderLayout.NORTH);
        showtimePanel.add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
    
        showtimeFrame.add(showtimePanel);
        showtimeFrame.setSize(400, 300);
        showtimeFrame.setLocationRelativeTo(null);
        showtimeFrame.setVisible(true);
    }
    

    // Seat Selection
    private void showSeatSelectionPage(Movie selectedMovie, int movieId, int showtimeId) {
        JFrame frame = new JFrame("Select Seat");
        JPanel panel = new JPanel(new GridLayout(5, 5, 5, 5));  // 5x5 grid for seats
        JButton[][] seatButtons = new JButton[5][5];
        
        //Fetch seatmap for the movie
        SeatMap seatMap = movieController.getSeatMapForMovie(movieId);  

        //Create a 5x5 grid of seats
        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col) {
                Seat seat = seatMap.getSeats().get(row * 5 + col);  
        
                seatButtons[row][col] = new JButton("Seat " + (row * 5 + col + 1));
                panel.add(seatButtons[row][col]);

                //Set occupied seats as red and unclickable, and available seats as green
                seatButtons[row][col].setOpaque(true);
                if (!seat.getAvailability()) {
                    seatButtons[row][col].setBackground(Color.RED);
                    seatButtons[row][col].setEnabled(false);  
                } else {
                    seatButtons[row][col].setBackground(Color.GREEN);
                }

                //Clicking on a seat will make it yellow and unclickable and will take user to paymenet page
                seatButtons[row][col].addActionListener((e) -> {
                    JButton selectedButton = (JButton) e.getSource();
                    selectedButton.setBackground(Color.YELLOW); 
                    selectedButton.setEnabled(false);  
                    showPaymentPage(selectedMovie, selectedButton.getText(), movieId, seat, showtimeId);
                    frame.dispose();
                });
            }
        }

        frame.add(panel);
        panel.revalidate();  
        panel.repaint();    
        frame.setSize(400, 400);
        frame.setVisible(true);
    }

    // Payment Page
    private void showPaymentPage(Movie selectedMovie, String selectedSeat, int movieId, Seat seat, int showtimeId) {
        JFrame paymentFrame = new JFrame("Payment");
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;

        //Input fields for payment information
        JLabel movieLabel = new JLabel("Movie: " + selectedMovie.getTitle());
        JLabel seatLabel = new JLabel("Seat: " + selectedSeat);
        JLabel nameLabel = new JLabel("Cardholder Name:");
        JTextField nameField = new JTextField(20);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberField = new JTextField(20);
        JLabel expiryLabel = new JLabel("Expiry Date (YYYY-MM-DD):");
        JTextField expiryField = new JTextField(10);
        JLabel cvvLabel = new JLabel("CVV:");
        JTextField cvvField = new JTextField(4);
        JLabel giftCardLabel = new JLabel("Gift Card:");
        JTextField giftCardField = new JTextField(20);
        JButton validateGiftCardButton = new JButton("Validate Gift Card");
        JLabel totalPriceLabel = new JLabel("Total Price: $0.00"); 
        JButton confirmButton = new JButton("Confirm Payment");

        //if user is already logged in, fill everything in
        if (currentUser != null) {
            PaymentInfo paymentInfo = database.getPaymentInfoForUser(currentUser.getUsername());
            if (paymentInfo != null) {
                nameField.setText(paymentInfo.getCardHolderName());
                cardNumberField.setText(paymentInfo.getCardNumber());
                expiryField.setText(paymentInfo.getExpiryDate());
                cvvField.setText(paymentInfo.getCvv());
                nameField.setEditable(false);
                cardNumberField.setEditable(false);
                expiryField.setEditable(false);
                cvvField.setEditable(false);
            }
        }

        //Add input fields
        paymentPanel.add(movieLabel, constraints);
        constraints.gridy = 1;
        paymentPanel.add(seatLabel, constraints);
        constraints.gridy = 2;
        paymentPanel.add(nameLabel, constraints);
        constraints.gridx = 1;
        paymentPanel.add(nameField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        paymentPanel.add(cardNumberLabel, constraints);
        constraints.gridx = 1;
        paymentPanel.add(cardNumberField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        paymentPanel.add(expiryLabel, constraints);
        constraints.gridx = 1;
        paymentPanel.add(expiryField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 5;
        paymentPanel.add(cvvLabel, constraints);
        constraints.gridx = 1;
        paymentPanel.add(cvvField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 6;
        paymentPanel.add(giftCardLabel, constraints);
        constraints.gridx = 1;
        paymentPanel.add(giftCardField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 7;
        paymentPanel.add(validateGiftCardButton, constraints);
        constraints.gridy = 8;
        paymentPanel.add(totalPriceLabel, constraints);
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        paymentPanel.add(confirmButton, constraints);

        //set ticket price
        double ticketPrice = 0.0;
        try {
            ticketPrice = database.getTicketPrice(selectedMovie.getMovieId());
            totalPriceLabel.setText(String.format("Total Price: $%.2f", ticketPrice));
        } catch (SQLException ex) {
            System.out.println("Error getting ticket price: " + ex);
        }
        final double initialTicketPrice = ticketPrice; 
        
        //Fields for gift card
        validateGiftCardButton.addActionListener((e) -> {
            String giftCardCode = giftCardField.getText();

            if (!giftCardCode.isEmpty()) {
                try {
                    //verify gift card validity
                    int giftCardID = Integer.parseInt(giftCardCode);
                    GiftCard giftCard = database.getGiftCardById(giftCardID);
                    if (giftCard != null && giftCard.getGiftCardBalance() > 0) {
                        double discountAmount = giftCard.getGiftCardBalance();
                        JOptionPane.showMessageDialog(paymentFrame, "Gift card validated", "Success", JOptionPane.INFORMATION_MESSAGE);
                        double updatedPrice = initialTicketPrice - discountAmount;
                        totalPriceLabel.setText(String.format("Total Price: $%.2f", updatedPrice));
                    } else {
                        JOptionPane.showMessageDialog(paymentFrame, "Invalid gift card or gift card has insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(paymentFrame, "Error validating gift card " + ex, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Please enter gift card code.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        //confirm payment
        confirmButton.addActionListener((e) -> {
            String name = nameField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryField.getText();
            String cvv = cvvField.getText();
            Integer giftCardID = null;

            //check if fields have correct values
            if (!name.isEmpty() && !cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty()) {
                if (cardNumber.length() == 16 && cvv.length() == 3 && expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    boolean paymentSuccess = false;
                    double finalTicketPrice = initialTicketPrice; 
                    //process payment and update credit card balance
                    if (currentUser != null) {
                        FinancialInstitution financialInstitution = new FinancialInstitution(this.database);
                        PaymentController paymentController = new PaymentController(financialInstitution);
                        PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryDate, name);
                        paymentSuccess = paymentController.processPayment(paymentInfo, finalTicketPrice);
                        if (paymentSuccess) {
                            try {
                                database.updateUserBalance(currentUser.getUsername(), finalTicketPrice);
                            } catch (SQLException ex) {
                                System.out.println("Error updating user balance: " + ex);
                            }
                        }
                    } else {
                        try {
                            database.insertCard(cardNumber, cvv, expiryDate, name);
                            paymentSuccess = true;
                        } catch (SQLException ex) {
                            System.out.println("Error adding card to database " + ex);
                        }
                    }

                    //create new ticket and change seat availability if payment is sucessful
                    if (paymentSuccess) {
                        seat.setAvailability(false);
                        movieController.updateSeatAvailability(seat);
                        Ticket ticket = new Ticket(selectedMovie, new Showtime(showtimeId, selectedMovie.getMovieId(), database.getShowtimeByMovie(movieId).getTime()), seat);
                        double finalPrice = Double.parseDouble(totalPriceLabel.getText().replace("Total Price: $", ""));
                        try {
                            String username = null;
                            if (getCurrentUser() != null) {
                                username = getCurrentUser().getUsername();
                            }
                            if (!giftCardField.getText().isEmpty()) {
                                giftCardID = Integer.parseInt(giftCardField.getText());
                            }
                            database.insertTicket(ticket, username, cardNumber, new Date());
                            if (giftCardID != null) {
                                database.deleteGiftCardById(giftCardID);
                                System.out.println("Gift card deleted from the database");
                            }
                        } catch (SQLException er) {
                            System.out.println("Error " + er);
                        }
                        //Generate receipt
                        String receiptContent = "Receipt:\n" +
                                "Movie: " + selectedMovie.getTitle() + "\n" +
                                "Seat: " + selectedSeat + "\n" +
                                String.format("Total Price: $%.2f\n", finalPrice) +
                                "Thank you";
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("receipt.txt"))) {
                            writer.write(receiptContent);
                            JOptionPane.showMessageDialog(paymentFrame, "Payment successful! Seat has been booked", "notification", JOptionPane.INFORMATION_MESSAGE);
                        } catch (IOException ioException) {
                            JOptionPane.showMessageDialog(paymentFrame, "Error saving receipt to file", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        paymentFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(paymentFrame, "Payment failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(paymentFrame, "Invalid card details or invalid date", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Please fill in payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        paymentFrame.add(paymentPanel);
        paymentFrame.setSize(500, 500);
        paymentFrame.setLocationRelativeTo(null);
        paymentFrame.setVisible(true);
    }
    


    public static void main(String[] args) {
        new ImageJFrame();
    }
}


