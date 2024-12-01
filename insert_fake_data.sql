-- Insert data into movies table


-- Insert data into users table
INSERT INTO users (username, name, address, password, isAnnualFeePaid, annualFeeDate) 
VALUES 
('john_doe', 'John Doe', '123 Main St', 'password123', TRUE, '2023-01-01'),
('jane_smith', 'Jane Smith', '456 Elm St', 'password456', FALSE, NULL),
('alice_jones', 'Alice Jones', '789 Oak St', 'password789', TRUE, '2023-06-15');

-- Insert data into paymentInfo table
INSERT INTO paymentInfo (cardNumber, cvv, expireDate, cardHolder, username, balance) 
VALUES 
('1234567890123456', '123', '2025-12-31', 'John Doe', 'john_doe', 100.00),
('2345678901234567', '234', '2024-11-30', 'Jane Smith', 'jane_smith', 200.00),
('3456789012345678', '345', '2023-10-31', 'Alice Jones', 'alice_jones', 150.00);

-- Insert data into seats table


-- Insert data into showtimes table


-- Insert data into tickets table


-- Insert data into giftCards table
INSERT INTO giftCards (giftCardID, giftCardBalance, expireDate) 
VALUES 
(1, 50, '2024-12-31'),
(2, 100, '2025-11-30'),
(3, 75, '2023-10-31');