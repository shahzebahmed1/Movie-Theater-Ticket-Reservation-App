-- this script inserts fake data into the AcmePlex database for our initial building and testing
-- run this in a mysqlworkbench query 
USE AcmePlex;

-- fake movies
INSERT INTO movies (movieID, title, duration, genre) VALUES
(1, 'Inception', 148, 'Sci-Fi'),
(2, 'The Dark Knight', 152, 'Action'),
(3, 'Interstellar', 169, 'Sci-Fi');

-- users
INSERT INTO users (userID, name, address, username, password) VALUES
(1, 'John Doe', '123 Elm Street', 'johndoe', 'password123'),
(2, 'Jane Smith', '456 Oak Avenue', 'janesmith', 'password456'),
(3, 'Alice Johnson', '789 Pine Road', 'alicejohnson', 'password789');

-- payment info
INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder) VALUES
('1234567812345678', '123', '2025-12-31', 'John Doe'),
('8765432187654321', '456', '2024-11-30', 'Jane Smith'),
('1122334455667788', '789', '2026-10-31', 'Alice Johnson');

-- seats
INSERT INTO seats (seatID, availability, seat_row, seat_column, movieID) VALUES
(1, TRUE, 1, 'A', 1),
(2, TRUE, 1, 'B', 1),
(3, TRUE, 1, 'C', 2),
(4, TRUE, 2, 'A', 2),
(5, TRUE, 2, 'B', 3),
(6, TRUE, 2, 'C', 3);

-- showimes
INSERT INTO showtimes (showtimeID, movieID, time) VALUES
(1, 1, '2023-12-01 19:00:00'),
(2, 2, '2023-12-01 21:00:00'),
(3, 3, '2023-12-02 18:00:00');

-- tickets
INSERT INTO tickets (seatID, showtimeID, movieID, userID) VALUES
(1, 1, 1, 1),
(2, 2, 2, 2),
(3, 3, 3, 3);