import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
        database = new Database("root", "password");
        movieController = new MovieController(database);

        // ActionListener for buttons
        bookTicketButton.addActionListener(e -> showMovieSelectionPage());
        createAccountButton.addActionListener(e -> showRegistrationMessage());
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

    // Show registration message (not implemented yet)
    private void showRegistrationMessage() {
        JOptionPane.showMessageDialog(mainFrame, "Registration screen not implemented yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
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

            if (username.equals("admin") && password.equals("admin123")) {
                JOptionPane.showMessageDialog(loginFrame, "Admin Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                userType = "admin";
                isAdmin = true;
                logoutButton.setVisible(true);
                adminControlsButton.setVisible(true);
                loginButton.setVisible(false);
                createAccountButton.setVisible(false);
                loginFrame.dispose();
            } else if (username.equals("user") && password.equals("user123")) {
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

        JPanel paymentPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel movieLabel = new JLabel("Movie: " + selectedMovie);
        JLabel seatLabel = new JLabel("Seat: " + selectedSeat);
        JLabel cardNumberLabel = new JLabel("Card Number:");
        JTextField cardNumberField = new JTextField();
        JLabel expiryDateLabel = new JLabel("Expiry Date (MM/YY):");
        JTextField expiryDateField = new JTextField();
        JLabel cvvLabel = new JLabel("CVV:");
        JTextField cvvField = new JTextField();
        JButton confirmButton = new JButton("Confirm Payment");

        confirmButton.addActionListener(e -> {
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(paymentFrame, "Please fill in all payment details.", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (cardNumber.length() != 16 || cvv.length() != 3) {
                JOptionPane.showMessageDialog(paymentFrame, "Invalid card details.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(paymentFrame, "Payment successful! Enjoy your movie.", "Success", JOptionPane.INFORMATION_MESSAGE);
                paymentFrame.dispose();
            }
        });

        paymentPanel.add(movieLabel);
        paymentPanel.add(new JLabel());
        paymentPanel.add(seatLabel);
        paymentPanel.add(new JLabel());
        paymentPanel.add(cardNumberLabel);
        paymentPanel.add(cardNumberField);
        paymentPanel.add(expiryDateLabel);
        paymentPanel.add(expiryDateField);
        paymentPanel.add(cvvLabel);
        paymentPanel.add(cvvField);
        paymentPanel.add(new JLabel());
        paymentPanel.add(confirmButton);

        paymentFrame.add(paymentPanel);
        paymentFrame.setSize(400, 400);
        paymentFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new ImageJFrame();
    }
}

