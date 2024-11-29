CREATE DATABASE IF NOT EXISTS AcmePlex; 

USE AcmePlex; 

CREATE TABLE IF NOT EXISTS movies ( movieID INT PRIMARY KEY, title VARCHAR(255), duration INT, genre VARCHAR(255) ); 

CREATE TABLE IF NOT EXISTS users ( userID INT PRIMARY KEY, name VARCHAR(255), address VARCHAR(255), username VARCHAR(255), password VARCHAR(255) ); 

CREATE TABLE IF NOT EXISTS paymentInfo( cardNumber VARCHAR(255), cvv VARCHAR(255), expireDate DATE, cardHolder VARCHAR(255) );

CREATE TABLE IF NOT EXISTS seats ( seatID INT primary key, availability BOOLEAN, seat_row INT, seat_column CHAR(1), movieID INT, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE ); 

create table if not exists showtimes( showtimeID INT primary key, movieID INT, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE cascade, time VARCHAR(255));

CREATE TABLE IF NOT EXISTS tickets ( seatID INT, showtimeID INT, movieID INT, userID INT, FOREIGN KEY (seatID) REFERENCES seats(seatID) ON DELETE cascade, FOREIGN KEY (showtimeID) REFERENCES showtimes(showtimeID) ON DELETE cascade, FOREIGN KEY (movieID) REFERENCES movies(movieID) ON DELETE CASCADE, FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE );