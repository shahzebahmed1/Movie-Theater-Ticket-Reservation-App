-- this script inserts fake data into the AcmePlex database for our initial building and testing
-- run this in a mysqlworkbench query 
USE AcmePlex;

-- fake movies
INSERT INTO movies (movieID, title, duration, genre, availableToPublic, preReleasedTicketsLeft) VALUES
(1, 'Inception', 148, 'Sci-Fi', TRUE, 2),
(2, 'The Dark Knight', 152, 'Action', TRUE, 2),
(3, 'Interstellar', 169, 'Sci-Fi', TRUE, 2);
(4, 'Shrek', 120, 'Comedy', TRUE, 2);
(5, 'Shutter Island', 120, 'Thriller', FALSE, 2);



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
INSERT INTO seats (seatID, availability, seat_row, seat_column, movieID) VALUES
(1, TRUE, 1, 'A', 1),
(2, TRUE, 1, 'B', 1),
(3, TRUE, 1, 'C', 2),
(4, TRUE, 2, 'A', 2),
(5, TRUE, 2, 'B', 3),
(6, TRUE, 2, 'C', 3);

-- showtimes
INSERT INTO showtimes (showtimeID, movieID, time) VALUES
(1, 1, '2023-12-01 19:00:00'),
(2, 2, '2023-12-01 21:00:00'),
(3, 3, '2023-12-02 18:00:00');

-- tickets
INSERT INTO tickets (seatID, showtimeID, movieID, username, dateBought, cardNumber) VALUES
(1, 1, 1, 'johndoe', '2023-11-01', '1234567812345678'),
(2, 2, 2, 'janesmith', '2023-11-02', '8765432187654321'),
(3, 3, 3, 'alicejohnson', '2023-11-03', '1122334455667788');