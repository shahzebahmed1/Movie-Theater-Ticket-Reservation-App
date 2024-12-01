-- this script inserts fake data into the AcmePlex database for our initial building and testing
-- run this in a mysqlworkbench query 
USE AcmePlex;

-- fake movies
--INSERT INTO movies (movieID, title, duration, genre, availableToPublic, preReleasedTicketsLeft) VALUES
--(1, 'Inception', 148, 'Sci-Fi', TRUE, 2),
--(2, 'The Dark Knight', 152, 'Action', TRUE, 2),
--(3, 'Interstellar', 169, 'Sci-Fi', TRUE, 2),
--(4, 'Shrek', 120, 'Comedy', TRUE, 2),
--(5, 'Shutter Island', 120, 'Thriller', FALSE, 2);
--If you want to add movies just add it through the admin tool, it will create the seats for it and the showtime. If you want to create diffent
--showtimes for a movie, just create another movie with the same title, genre,etc but with diff showtime


-- users
INSERT INTO users (username, name, address, password, balance, isAnnualFeePaid, annualFeeDate) VALUES
('johndoe', 'John Doe', '123 Elm Street', 'password123', 100.00, TRUE, '2023-01-01'),
('janesmith', 'Jane Smith', '456 Oak Avenue', 'password456', 150.00, TRUE, '2023-02-01'),
('alicejohnson', 'Alice Johnson', '789 Pine Road', 'password789', 200.00, TRUE, '2023-03-01');



-- payment info
INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder, username) VALUES
('1234567812345678', '123', '2025-12-31', 'John Doe', 'johndoe'),
('8765432187654321', '456', '2024-11-30', 'Jane Smith', 'janesmith'),
('1122334455667788', '789', '2026-10-31', 'Alice Johnson', 'alicejohnson');

-- seats
--INSERT INTO seats (seatID, availability, seat_row, seat_column, movieID) VALUES



-- showtimes
--INSERT INTO showtimes (showtimeID, movieID, time) VALUES


-- tickets
--INSERT INTO tickets (seatID, showtimeID, movieID, username, dateBought, cardNumber) VALUES
