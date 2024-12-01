import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    private ArrayList<String> movies = new ArrayList<>(); // Placeholder for movies
    private ArrayList<String> users = new ArrayList<>(); // Placeholder for users

    private Database database;
    private MovieController movieController;
    private PaymentController paymentController;

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

        // Add the layered pane to the frame
        mainFrame.add(layeredPane, BorderLayout.CENTER);

        // Frame settings
        mainFrame.setSize(875, 500);
        mainFrame.setResizable(false);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
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

        JPanel invoicePanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JLabel invoiceLabel = new JLabel("Enter Invoice Number:");
        JTextField invoiceField = new JTextField();
        JButton lookupButton = new JButton("Lookup");

        invoicePanel.add(invoiceLabel);
        invoicePanel.add(invoiceField);
        invoicePanel.add(lookupButton);

        lookupButton.addActionListener(e -> {
            String invoiceNumber = invoiceField.getText();
            // Simulate invoice lookup
            if (invoiceNumber.equals("12345")) { // Replace with database logic
                int option = JOptionPane.showConfirmDialog(invoiceFrame, "Invoice found! Do you want to cancel the ticket for a refund?", "Invoice Lookup", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(invoiceFrame, "Ticket canceled. Refund issued.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    invoiceFrame.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(invoiceFrame, "Invalid Invoice Number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        invoiceFrame.add(invoicePanel);
        invoiceFrame.setSize(400, 200);
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

    // Movie Selection
    private void showMovieSelectionPage() {
        JFrame movieFrame = new JFrame("Select Movie");

        JPanel moviePanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());

        searchField = new JTextField(20);
        searchButton = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        ArrayList<Movie> allMovies = movieController.browseMovies();
        ArrayList<String> movieTitles = new ArrayList<>();
        for (Movie movie : allMovies) {
            movieTitles.add(movie.getTitle());
        }

        JList<String> movieList = new JList<>(movieTitles.toArray(new String[0]));
        movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            String selectedMovie = movieList.getSelectedValue();
            if (selectedMovie != null) {
                showSeatSelectionPage(selectedMovie);
                movieFrame.dispose();
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
    private void showSeatSelectionPage(String selectedMovie) {
        JFrame seatFrame = new JFrame("Select Seat");

        JPanel seatPanel = new JPanel(new GridLayout(5, 5, 5, 5)); // 5x5 grid for seats
        JButton[][] seats = new JButton[5][5];

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 5; col++) {
                seats[row][col] = new JButton("Seat " + (row * 5 + col + 1));
                seatPanel.add(seats[row][col]);
                seats[row][col].addActionListener(e -> {
                    JButton selectedSeat = (JButton) e.getSource();
                    selectedSeat.setBackground(Color.GREEN);
                    selectedSeat.setEnabled(false);
                    showPaymentPage(selectedMovie, selectedSeat.getText());
                    seatFrame.dispose();
                });
            }
        }

        seatFrame.add(seatPanel);
        seatFrame.setSize(400, 400);
        seatFrame.setVisible(true);
    }

    // Payment Page
    private void showPaymentPage(String selectedMovie, String selectedSeat) {
        JFrame paymentFrame = new JFrame("Payment");

        JPanel paymentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel movieLabel = new JLabel("Movie: " + selectedMovie);
        JLabel seatLabel = new JLabel("Seat: " + selectedSeat);
        JLabel cardholderLabel = new JLabel("Cardholder Name:");
        JTextField cardholderField = new JTextField(20);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberField = new JTextField(20);
        JLabel expiryDateLabel = new JLabel("Expiry Date (YYYY-MM-DD):");
        JTextField expiryDateField = new JTextField(10);
        JLabel cvvLabel = new JLabel("CVV:");
        JTextField cvvField = new JTextField(4);
        JButton confirmButton = new JButton("Confirm Payment");

        gbc.gridx = 0;
        gbc.gridy = 0;
        paymentPanel.add(movieLabel, gbc);

        gbc.gridy = 1;
        paymentPanel.add(seatLabel, gbc);

        gbc.gridy = 2;
        paymentPanel.add(cardholderLabel, gbc);
        gbc.gridx = 1;
        cardholderField.setPreferredSize(new Dimension(300, 30));
        paymentPanel.add(cardholderField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 3;
        paymentPanel.add(cardNumberLabel, gbc);
        gbc.gridx = 1;
        cardNumberField.setPreferredSize(new Dimension(300, 30));
        paymentPanel.add(cardNumberField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 4;
        paymentPanel.add(expiryDateLabel, gbc);
        gbc.gridx = 1;
        expiryDateField.setPreferredSize(new Dimension(300, 30));
        paymentPanel.add(expiryDateField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 5;
        paymentPanel.add(cvvLabel, gbc);
        gbc.gridx = 1;
        cvvField.setPreferredSize(new Dimension(300, 30));
        paymentPanel.add(cvvField, gbc);
        gbc.gridx = 0;

        gbc.gridy = 6;
        gbc.gridwidth = 2;
        paymentPanel.add(confirmButton, gbc);

        confirmButton.addActionListener(e -> {
            String cardholderName = cardholderField.getText();
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            if (cardholderName.isEmpty() || cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame, "Please fill in payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (cardNumber.length() != 16 || cvv.length() != 3 || !expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(paymentFrame, "Invalid card details or invalid date format", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                FinancialInstitution financialInstitution = new FinancialInstitution(database);
                PaymentController paymentController = new PaymentController(financialInstitution);

                double amount = 15.00;
                boolean paymentSuccess = paymentController.processPayment(cardNumber, cvv, expiryDate, cardholderName, amount);

                if (paymentSuccess) {
                    JOptionPane.showMessageDialog(paymentFrame, "Payment successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    paymentFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(paymentFrame, "Payment failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        paymentFrame.add(paymentPanel);
        paymentFrame.setSize(500, 400); // Adjusted frame size
        paymentFrame.setLocationRelativeTo(null);
        paymentFrame.setVisible(true);
    }



    public static void main(String[] args) {
        new ImageJFrame();
    }
}


