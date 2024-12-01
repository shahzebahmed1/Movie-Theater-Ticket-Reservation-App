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
-- Movie 1
(1, TRUE, 1, 'A', 1),
(2, TRUE, 1, 'B', 1),
(3, TRUE, 1, 'C', 1),
(4, TRUE, 1, 'D', 1),
(5, TRUE, 1, 'E', 1),
(6, TRUE, 2, 'A', 1),
(7, TRUE, 2, 'B', 1),
(8, TRUE, 2, 'C', 1),
(9, TRUE, 2, 'D', 1),
(10, TRUE, 2, 'E', 1),
(11, TRUE, 3, 'A', 1),
(12, TRUE, 3, 'B', 1),
(13, TRUE, 3, 'C', 1),
(14, TRUE, 3, 'D', 1),
(15, TRUE, 3, 'E', 1),
(16, TRUE, 4, 'A', 1),
(17, TRUE, 4, 'B', 1),
(18, TRUE, 4, 'C', 1),
(19, TRUE, 4, 'D', 1),
(20, TRUE, 4, 'E', 1),
(21, TRUE, 5, 'A', 1),
(22, TRUE, 5, 'B', 1),
(23, TRUE, 5, 'C', 1),
(24, TRUE, 5, 'D', 1),
(25, TRUE, 5, 'E', 1),

-- Movie 2
(26, TRUE, 1, 'A', 2),
(27, TRUE, 1, 'B', 2),
(28, TRUE, 1, 'C', 2),
(29, TRUE, 1, 'D', 2),
(30, TRUE, 1, 'E', 2),
(31, TRUE, 2, 'A', 2),
(32, TRUE, 2, 'B', 2),
(33, TRUE, 2, 'C', 2),
(34, TRUE, 2, 'D', 2),
(35, TRUE, 2, 'E', 2),
(36, TRUE, 3, 'A', 2),
(37, TRUE, 3, 'B', 2),
(38, TRUE, 3, 'C', 2),
(39, TRUE, 3, 'D', 2),
(40, TRUE, 3, 'E', 2),
(41, TRUE, 4, 'A', 2),
(42, TRUE, 4, 'B', 2),
(43, TRUE, 4, 'C', 2),
(44, TRUE, 4, 'D', 2),
(45, TRUE, 4, 'E', 2),
(46, TRUE, 5, 'A', 2),
(47, TRUE, 5, 'B', 2),
(48, TRUE, 5, 'C', 2),
(49, TRUE, 5, 'D', 2),
(50, TRUE, 5, 'E', 2),

-- Movie 3
(51, TRUE, 1, 'A', 3),
(52, TRUE, 1, 'B', 3),
(53, TRUE, 1, 'C', 3),
(54, TRUE, 1, 'D', 3),
(55, TRUE, 1, 'E', 3),
(56, TRUE, 2, 'A', 3),
(57, TRUE, 2, 'B', 3),
(58, TRUE, 2, 'C', 3),
(59, TRUE, 2, 'D', 3),
(60, TRUE, 2, 'E', 3),
(61, TRUE, 3, 'A', 3),
(62, TRUE, 3, 'B', 3),
(63, TRUE, 3, 'C', 3),
(64, TRUE, 3, 'D', 3),
(65, TRUE, 3, 'E', 3),
(66, TRUE, 4, 'A', 3),
(67, TRUE, 4, 'B', 3),
(68, TRUE, 4, 'C', 3),
(69, TRUE, 4, 'D', 3),
(70, TRUE, 4, 'E', 3),
(71, TRUE, 5, 'A', 3),
(72, TRUE, 5, 'B', 3),
(73, TRUE, 5, 'C', 3),
(74, TRUE, 5, 'D', 3),
(75, TRUE, 5, 'E', 3),

-- Movie 4
(76, TRUE, 1, 'A', 4),
(77, TRUE, 1, 'B', 4),
(78, TRUE, 1, 'C', 4),
(79, TRUE, 1, 'D', 4),
(80, TRUE, 1, 'E', 4),
(81, TRUE, 2, 'A', 4),
(82, TRUE, 2, 'B', 4),
(83, TRUE, 2, 'C', 4),
(84, TRUE, 2, 'D', 4),
(85, TRUE, 2, 'E', 4),
(86, TRUE, 3, 'A', 4),
(87, TRUE, 3, 'B', 4),
(88, TRUE, 3, 'C', 4),
(89, TRUE, 3, 'D', 4),
(90, TRUE, 3, 'E', 4),
(91, TRUE, 4, 'A', 4),
(92, TRUE, 4, 'B', 4),
(93, TRUE, 4, 'C', 4),
(94, TRUE, 4, 'D', 4),
(95, TRUE, 4, 'E', 4),
(96, TRUE, 5, 'A', 4),
(97, TRUE, 5, 'B', 4),
(98, TRUE, 5, 'C', 4),
(99, TRUE, 5, 'D', 4),
(100, TRUE, 5, 'E', 4),

-- Movie 5
(101, TRUE, 1, 'A', 5),
(102, TRUE, 1, 'B', 5),
(103, TRUE, 1, 'C', 5),
(104, TRUE, 1, 'D', 5),
(105, TRUE, 1, 'E', 5),
(106, TRUE, 2, 'A', 5),
(107, TRUE, 2, 'B', 5),
(108, TRUE, 2, 'C', 5),
(109, TRUE, 2, 'D', 5),
(110, TRUE, 2, 'E', 5),
(111, TRUE, 3, 'A', 5),
(112, TRUE, 3, 'B', 5),
(113, TRUE, 3, 'C', 5),
(114, TRUE, 3, 'D', 5),
(115, TRUE, 3, 'E', 5),
(116, TRUE, 4, 'A', 5),
(117, TRUE, 4, 'B', 5),
(118, TRUE, 4, 'C', 5),
(119, TRUE, 4, 'D', 5),
(120, TRUE, 4, 'E', 5),
(121, TRUE, 5, 'A', 5),
(122, TRUE, 5, 'B', 5),
(123, TRUE, 5, 'C', 5),
(124, TRUE, 5, 'D', 5),
(125, TRUE, 5, 'E', 5);


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