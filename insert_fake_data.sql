-- this script inserts fake data into the AcmePlex database for our initial building and testing
-- run this in a mysqlworkbench query 
USE AcmePlex;

-- fake movies
-- INSERT INTO movies (movieID, title, duration, genre, availableToPublic, preReleasedTicketsLeft) VALUES
-- (1, 'Inception', 148, 'Sci-Fi', TRUE, 2),
-- (2, 'The Dark Knight', 152, 'Action', TRUE, 2),
-- (3, 'Interstellar', 169, 'Sci-Fi', TRUE, 2),
-- (4, 'Shrek', 120, 'Comedy', TRUE, 2),
-- (5, 'Shutter Island', 120, 'Thriller', FALSE, 2);
-- If you want to add movies just add it through the admin tool, it will create the seats for it and the showtime. If you want to create diffent
-- showtimes for a movie, just create another movie with the same title, genre,etc but with diff showtime


-- users
INSERT INTO users (username, name, address, password, isAnnualFeePaid, annualFeeDate) VALUES
('johndoe', 'John Doe', '123 Elm Street', 'password123', TRUE, '2023-01-01'),
('janesmith', 'Jane Smith', '456 Oak Avenue', 'password456', TRUE, '2023-02-01'),
('alicejohnson', 'Alice Johnson', '789 Pine Road', 'password789', TRUE, '2023-03-01');



-- Insert data into paymentInfo table
INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder, username, balance) VALUES 
('1234567890123456', '123', '2025-12-31', 'John Doe', 'johndoe', 100.00),
('2345678901234567', '234', '2024-11-30', 'Jane Smith', 'janesmith', 200.00),
('3456789012345678', '345', '2023-10-31', 'Alice Jones', 'alicejohnson', 150.00);

-- Insert data into seats table


-- Insert data into showtimes table


-- Insert data into tickets table


-- Insert data into giftCards table
INSERT INTO giftCards (giftCardID, giftCardBalance, expireDate) 
VALUES 
(1, 50, '2024-12-31'),
(2, 100, '2025-11-30'),
(3, 75, '2023-10-31');