CREATE DATABASE IF NOT EXISTS AcmePlex; 

USE AcmePlex; 

CREATE TABLE IF NOT EXISTS movies ( movieID INT PRIMARY KEY, title VARCHAR(255), duration INT, genre VARCHAR(255), availableToPublic BOOLEAN, preReleasedTicketsLeft INT ); 

CREATE TABLE IF NOT EXISTS users ( username VARCHAR(255) PRIMARY KEY, name VARCHAR(255), address VARCHAR(255), password VARCHAR(255), balance DECIMAL(8, 2), isAnnualFeePaid BOOLEAN, annualFeeDate DATE); 

CREATE TABLE IF NOT EXISTS paymentInfo( cardNumber VARCHAR(255) PRIMARY KEY, cvv VARCHAR(255), expireDate DATE, cardHolder VARCHAR(255), username VARCHAR(255), FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS seats ( seatID INT PRIMARY KEY, availability BOOLEAN, seat_row INT, seat_column CHAR(1), movieID INT, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE ); 

CREATE TABLE IF NOT EXISTS showtimes( showtimeID INT primary key, movieID INT, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE cascade, time VARCHAR(255));

CREATE TABLE IF NOT EXISTS tickets ( ticketID INT AUTO_INCREMENT PRIMARY KEY, seatID INT, showtimeID INT, movieID INT, username VARCHAR(255), dateBought DATE, cardNumber VARCHAR(255), FOREIGN KEY (seatID) REFERENCES seats(seatID) ON DELETE cascade, FOREIGN KEY (showtimeID) REFERENCES showtimes(showtimeID) ON DELETE cascade, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE, FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE, FOREIGN KEY (cardNumber) REFERENCES paymentInfo(cardNumber) ON DELETE CASCADE );
