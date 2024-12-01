import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;


public class ImageJFrame {
    private String userType = "guest"; // Default user type is "guest"
    private boolean isAdmin = false; // Tracks if the current user is an admin
    private JButton logoutButton; // Logout button (only shown when logged in)
    private JButton orderHistoryButton, invoiceLookupButton, adminControlsButton;
    private JFrame mainFrame;
    private JTextField searchField;
    private JButton searchButton;
    private JButton loginButton;
    private JButton createAccountButton;
    private User currentUser; // Add a field to store the current user

    private ArrayList<String> movies = new ArrayList<>(); // Placeholder for movies
    private ArrayList<String> users = new ArrayList<>(); // Placeholder for users

    private Database database;
    private MovieController movieController;

    ImageJFrame() {
        // Add some default movies for testing
        movies.add("Movie 1 - 12:00 PM");
        movies.add("Movie 2 - 3:00 PM");
        movies.add("Movie 3 - 6:00 PM");

        // Add some default users
        users.add("user1");
        users.add("user2");

        // Create JFrame
        mainFrame = new JFrame("AcmePlex");
        mainFrame.setLayout(new BorderLayout());

        // Create a layered pane for overlaying the image and buttons
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(875, 500));

        // Create a JLabel with a scaled image
        ImageIcon originalIcon = new ImageIcon("AcmePlex1.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(875, 500, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setBounds(0, 0, 875, 500); // Full-size background image
        layeredPane.add(imageLabel, Integer.valueOf(0)); // Add image at the background layer

        // Create a JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make the panel transparent
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setBounds(0, 0, 875, 500); // Same size as the image

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Create buttons
        JButton bookTicketButton = new JButton("Book Ticket");
        orderHistoryButton = new JButton("Order History");
        orderHistoryButton.setVisible(false); // Hidden by default
        invoiceLookupButton = new JButton("Invoice Lookup");
        createAccountButton = new JButton("Register");
        loginButton = new JButton("Login");
        logoutButton = new JButton("Logout");
        logoutButton.setVisible(false); // Hidden by default
        adminControlsButton = new JButton("Admin Controls");
        adminControlsButton.setVisible(false); // Visible only for admins

        // Add buttons to the button panel
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

        // Add the button panel to the layered pane
        layeredPane.add(buttonPanel, Integer.valueOf(1)); // Add buttons at the foreground layer

        // Initialize database and movie controller
        database = new Database("root", "password"); // use your credentials!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        movieController = new MovieController(database);

        // ActionListener for buttons
        bookTicketButton.addActionListener(e -> showMovieSelectionPage());
        createAccountButton.addActionListener(e -> showRegistrationForm());
        loginButton.addActionListener(e -> showLoginFrame(loginButton, createAccountButton));
        logoutButton.addActionListener(e -> logout());
        invoiceLookupButton.addActionListener(e -> showInvoiceLookupFrame());
        adminControlsButton.addActionListener(e -> showAdminControls());
        orderHistoryButton.addActionListener(e -> {
            if (userType.equals("user")) {
                showOrderHistory(currentUser); // Ensure currentUser is passed correctly
            }
        });

        // Add the layered pane to the frame
        mainFrame.add(layeredPane, BorderLayout.CENTER);

        // Frame settings
        mainFrame.setSize(875, 500);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    private void showOrderHistory(User user) {
        JFrame orderHistoryFrame = new JFrame("Order History");
        JPanel orderHistoryPanel = new JPanel(new BorderLayout());

        JTextArea orderHistoryTextArea = new JTextArea();
        orderHistoryTextArea.setEditable(false);

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

    private User getCurrentUser() {
        return currentUser;
    }

    // User login logic
    private void showLoginFrame(JButton loginButton, JButton createAccountButton) {
        JFrame loginFrame = new JFrame("Login");
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        JButton loginSubmitButton = new JButton("Login");

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

            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(loginFrame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                userType = "admin";
                isAdmin = true;
                logoutButton.setVisible(true);
                adminControlsButton.setVisible(true);
                loginButton.setVisible(false);
                createAccountButton.setVisible(false);
                loginFrame.dispose();
            } else
                try {
                    if (u.login(database, username, password)) {
                        JOptionPane.showMessageDialog(loginFrame, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        userType = "user";
                        currentUser = u; 
                        currentUser.setUsername(username); // Ensure the username is set for the current user
                        logoutButton.setVisible(true);
                        orderHistoryButton.setVisible(true);
                        loginButton.setVisible(false);
                        createAccountButton.setVisible(false);
                        loginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (HeadlessException | SQLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
        });

        loginFrame.add(loginPanel);
        loginFrame.setSize(400, 200);
        loginFrame.setVisible(true);
    }

    // Logout logic
    private void logout() {
        userType = "guest";
        isAdmin = false;
        currentUser = null; // Clear the current user
        JOptionPane.showMessageDialog(mainFrame, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);
        logoutButton.setVisible(false);
        orderHistoryButton.setVisible(false);
        adminControlsButton.setVisible(false);
        loginButton.setVisible(true);
        createAccountButton.setVisible(true);

        mainFrame.repaint();
    }

    private void showRegistrationForm() {
        JFrame registerFrame = new JFrame("Register");
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set default spacing for components
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        registerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        JTextField usernameField = new JTextField(15);
        registerPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 3;
        JPasswordField passwordField = new JPasswordField(15);
        registerPanel.add(passwordField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        registerPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(15);
        registerPanel.add(nameField, gbc);

        // Address
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 3;
        JTextField addressField = new JTextField(15);
        registerPanel.add(addressField, gbc);

        // Initial Balance
        gbc.gridx = 0;
        gbc.gridy = 2;
        registerPanel.add(new JLabel("Initial Balance:"), gbc);
        gbc.gridx = 1;
        JTextField balanceField = new JTextField(15);
        registerPanel.add(balanceField, gbc);

        // Card Number
        gbc.gridx = 2;
        registerPanel.add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 3;
        JTextField cardNumberField = new JTextField(15);
        registerPanel.add(cardNumberField, gbc);

        // Expiry Date
        gbc.gridx = 0;
        gbc.gridy = 3;
        registerPanel.add(new JLabel("Expiry Date (YYYY-DD-MM):"), gbc);
        gbc.gridx = 1;
        JTextField expiryDateField = new JTextField(15);
        registerPanel.add(expiryDateField, gbc);

        // CVV
        gbc.gridx = 2;
        registerPanel.add(new JLabel("CVV:"), gbc);
        gbc.gridx = 3;
        JTextField cvvField = new JTextField(15);
        registerPanel.add(cvvField, gbc);

        // Register Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton registerButton = new JButton("Register");
        registerPanel.add(registerButton, gbc);

        // ActionListener for registration
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String name = nameField.getText();
            String address = addressField.getText();
            String balanceText = balanceField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            // Validate inputs
            if (username.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty() || balanceText.isEmpty() ||
                    cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(registerFrame, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // check for a valid date
            if (!validDate(expiryDate)) {
            	JOptionPane.showMessageDialog(registerFrame, "Please enter a valid date", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Ensure balance is a valid number
            double balance;
            try {
                balance = Double.parseDouble(balanceText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(registerFrame, "Invalid balance amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Basic validation for card number and CVV
            if (cardNumber.length() != 16 || cvv.length() != 3) {
                JOptionPane.showMessageDialog(registerFrame, "Invalid card details.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Simulate user registration
            User u = new User();
            
            u.register(database, username, password, name, address, balance, cardNumber, cvv, expiryDate);
            
            JOptionPane.showMessageDialog(registerFrame, "Registration successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            registerFrame.dispose();
        });

        // Add the panel to the frame
        registerFrame.add(registerPanel);
        registerFrame.pack();
        registerFrame.setLocationRelativeTo(null); // Center the frame
        registerFrame.setVisible(true);
    }

    // helper function to see if date is valid
    public boolean validDate(String date) {
    	
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // format the date should be
        try {
            LocalDate d = LocalDate.parse(date, formatter);
            return true; // if we can parse the data, then it is a valid date
        } catch (DateTimeParseException e) {
            return false; // otherwise its invalid
        }
    }

    // Show Invoice Lookup Frame
    private void showInvoiceLookupFrame() {
        JFrame invoiceFrame = new JFrame("Invoice Lookup");

        JPanel invoicePanel = new JPanel(new GridLayout(5, 1, 10, 10)); // Adjusted to 5 rows
        JLabel invoiceLabel = new JLabel("Enter Invoice (ticketID) Number:");
        JTextField invoiceField = new JTextField();
        JLabel cardNumberLabel = new JLabel("Confirm Card Number Used to Pay:");
        JTextField cardNumberField = new JTextField();
        JButton lookupButton = new JButton("Lookup");

        invoicePanel.add(invoiceLabel);
        invoicePanel.add(invoiceField);
        invoicePanel.add(cardNumberLabel);
        invoicePanel.add(cardNumberField);
        invoicePanel.add(lookupButton);

        lookupButton.addActionListener(e -> {
            String invoiceNumber = invoiceField.getText();
            String cardNumber = cardNumberField.getText();
            try {
                int ticketID = Integer.parseInt(invoiceNumber);
                boolean ticketFound = database.checkTicketExists(ticketID);
                if (ticketFound) {
                    boolean cardMatches = database.checkCardNumberForTicket(ticketID, cardNumber);
                    if (cardMatches) {
                        int option = JOptionPane.showConfirmDialog(invoiceFrame, "Invoice found! Do you want to cancel the ticket for a refund?", "Invoice Lookup", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            Seat seat = database.getSeatForTicket(ticketID); 
                        
                            seat.setAvailability(true);
                            movieController.updateSeatAvailability(seat);
                            database.cancelTicket(ticketID);

                            
                            JOptionPane.showMessageDialog(invoiceFrame, "Ticket canceled. Refund issued.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            invoiceFrame.dispose();
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
        JButton manageShowtimesButton = new JButton("Manage Showtimes");
        JButton deleteUserButton = new JButton("Delete User");

        adminPanel.add(addMovieButton);
        adminPanel.add(deleteMovieButton);
        adminPanel.add(manageShowtimesButton);
        adminPanel.add(deleteUserButton);

        addMovieButton.addActionListener(e -> addMovie());
        deleteMovieButton.addActionListener(e -> deleteMovie());
        manageShowtimesButton.addActionListener(e -> manageShowtimes());
        deleteUserButton.addActionListener(e -> deleteUser());

        adminFrame.add(adminPanel);
        adminFrame.setSize(400, 300);
        adminFrame.setVisible(true);
    }

    // Add Movie
    private void addMovie() {
        String newMovie = JOptionPane.showInputDialog(mainFrame, "Enter Movie Name and Showtime:");
        if (newMovie != null && !newMovie.trim().isEmpty()) {
            movies.add(newMovie);
            JOptionPane.showMessageDialog(mainFrame, "Movie added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Invalid movie details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete Movie
    private void deleteMovie() {
        String[] movieArray = movies.toArray(new String[0]);
        String selectedMovie = (String) JOptionPane.showInputDialog(mainFrame, "Select a movie to delete:", "Delete Movie",
                JOptionPane.QUESTION_MESSAGE, null, movieArray, movieArray[0]);
        if (selectedMovie != null) {
            movies.remove(selectedMovie);
            JOptionPane.showMessageDialog(mainFrame, "Movie deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Manage Showtimes
    private void manageShowtimes() {
        JOptionPane.showMessageDialog(mainFrame, "Showtime management not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    // Delete User
    private void deleteUser() {
        String[] userArray = users.toArray(new String[0]);
        String selectedUser = (String) JOptionPane.showInputDialog(mainFrame, "Select a user to delete:", "Delete User",
                JOptionPane.QUESTION_MESSAGE, null, userArray, userArray[0]);
        if (selectedUser != null) {
            users.remove(selectedUser);
            JOptionPane.showMessageDialog(mainFrame, "User deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
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
    
        ArrayList<Movie> allMovies;
        if(userType.equals("guest")){
            allMovies = movieController.browseMovies(true);
        } else {
            allMovies = movieController.browseMovies(false);
        }
    
        // Instead of just storing movie titles, store Movie objects in the list
        ArrayList<Movie> movieListData = new ArrayList<>(allMovies);
        ArrayList<String> movieTitles = new ArrayList<>();
        for (Movie movie : allMovies) {
            movieTitles.add(movie.getTitle());
        }
    
        JList<String> movieList = new JList<>(movieTitles.toArray(new String[0]));
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            String selectedTitle = movieList.getSelectedValue();
            if (selectedTitle != null) {
                // Find the corresponding Movie object based on selected title
                Movie selectedMovie = null;
                for (Movie movie : movieListData) {
                    if (movie.getTitle().equals(selectedTitle)) {
                        selectedMovie = movie;
                        break;
                    }
                }
    
                if (selectedMovie != null) {
                    int movieId = selectedMovie.getMovieId();
                    showSeatSelectionPage(selectedMovie, movieId);
                    movieFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(movieFrame, "Error finding selected movie.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(movieFrame, "Please select a movie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                ArrayList<String> searchResults = new ArrayList<>();
                for (Movie movie : allMovies) {
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
    
        moviePanel.add(new JLabel("Select a Movie and Showtime:"), BorderLayout.NORTH);
        moviePanel.add(searchPanel, BorderLayout.NORTH);
        moviePanel.add(new JScrollPane(movieList), BorderLayout.CENTER);
        moviePanel.add(nextButton, BorderLayout.SOUTH);
    
        movieFrame.add(moviePanel);
        movieFrame.setSize(400, 300);
        movieFrame.setVisible(true);
    }
    

    // Seat Selection
    private void showSeatSelectionPage(Movie selectedMovie, int movieId) {
        JFrame frame = new JFrame("Select Seat");
        JPanel panel = new JPanel(new GridLayout(5, 5, 5, 5));  // 5x5 grid for seats
        JButton[][] seatButtons = new JButton[5][5];
        
        SeatMap seatMap = movieController.getSeatMapForMovie(movieId);  

        for (int row = 0; row < 5; ++row) {
            for (int col = 0; col < 5; ++col) {
                Seat seat = seatMap.getSeats().get(row * 5 + col);  
        
                seatButtons[row][col] = new JButton("Seat " + (row * 5 + col + 1));
                panel.add(seatButtons[row][col]);

                seatButtons[row][col].setOpaque(true);
                if (!seat.getAvailability()) {
                    seatButtons[row][col].setBackground(Color.RED);
                    seatButtons[row][col].setEnabled(false);  
                } else {
                    seatButtons[row][col].setBackground(Color.GREEN);
                }

                seatButtons[row][col].addActionListener((e) -> {
                    JButton selectedButton = (JButton) e.getSource();
        
                    selectedButton.setBackground(Color.YELLOW); 
                    selectedButton.setEnabled(false);  

                    showPaymentPage(selectedMovie, selectedButton.getText(), movieId, seat);
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
    private void showPaymentPage(Movie selectedMovie, String selectedSeat, int movieId, Seat seat) {
        JFrame paymentFrame = new JFrame("Payment");
        JPanel paymentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.anchor = GridBagConstraints.WEST;
    
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
        JButton confirmButton = new JButton("Confirm Payment");
    
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
        constraints.gridwidth = 2;
        paymentPanel.add(confirmButton, constraints);
    
        confirmButton.addActionListener((e) -> {
            String name = nameField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryField.getText();
            String cvv = cvvField.getText();
    
            if (!name.isEmpty() && !cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty()) {
                if (cardNumber.length() == 16 && cvv.length() == 3 && expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    FinancialInstitution financialInstitution = new FinancialInstitution(this.database);
                    PaymentController paymentController = new PaymentController(financialInstitution);
                    double amount = 15.0; 
                    PaymentInfo paymentInfo = new PaymentInfo(cardNumber, cvv, expiryDate, name);
                    boolean paymentSuccess = paymentController.processPayment(paymentInfo, amount);
    
                    if (paymentSuccess) {
                        seat.setAvailability(false); 
                        movieController.updateSeatAvailability(seat);
                        Ticket ticket = new Ticket(selectedMovie, new Showtime(1,selectedMovie.getMovieId(),"2023-12-01 19:00:00"), seat);  // Assuming selectedShowtime exists
                        try {
                            String username = null;
                            if (getCurrentUser() != null) {
                                username = getCurrentUser().getUsername();
                            }
                            database.insertTicket(ticket, username, paymentInfo.getCardNumber(), new Date());
                        } catch (SQLException er) {
                            System.out.println("Error " + er);
                        }
    
                        JOptionPane.showMessageDialog(paymentFrame, "Payment successful! Seat has been booked.", "Success", JOptionPane.INFORMATION_MESSAGE);
                        paymentFrame.dispose();  
                    } else {
                        JOptionPane.showMessageDialog(paymentFrame, "Payment failed.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(paymentFrame, "Invalid card details or invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Please fill in payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    
        paymentFrame.add(paymentPanel);
        paymentFrame.setSize(500, 400);
        paymentFrame.setLocationRelativeTo(null);
        paymentFrame.setVisible(true);
    }
    



    public static void main(String[] args) {
        new ImageJFrame();
    }
}


