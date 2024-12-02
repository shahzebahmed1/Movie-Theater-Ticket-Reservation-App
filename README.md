# ENSF 480 - Principles of Software Design
## Movie Theater Ticket Reservation App

## Project Overview

Welcome to the Movie Ticket Web Application project developed by Group 30 for the ENSF 480 course.

First download the .java and .class files, as well as the mysql-connector-j-8.0.31.jar. 

Then set up the MySQL. In line 114 of ImageJFrame.java, you may have to replace the user and password with your own login if it isnt 'root' and 'password'. You will have to recompile as well if you change this. In your MySQLWorkbench, first run the database_creation.sql query. After that, run the ImageJFrame with the database:
To run ImageJFrame with the db: `java -cp .:mysql-connector-j-8.0.31.jar ImageJFrame`. 

To begin, you may want to first login as an admin user, using "admin" and "admin123". This will allow you to create movies, which will implicitly create seat and showtime data as well. 

Other useful commands:
Compile all java classes with the mysql jdbc driver: `javac -cp .:mysql-connector-j-8.0.31.jar *.java`
