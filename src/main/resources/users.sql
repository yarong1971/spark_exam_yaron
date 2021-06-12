--CREATE DATABASE IF NOT EXISTS game_platform;
--USE game_platform;

CREATE TABLE IF NOT EXISTS users (
                               id INT NOT NULL,
                               name VARCHAR (100) NOT NULL ,
                               lastName VARCHAR (100) NOT NULL,
                               countryOfOrigin VARCHAR(50) NOT NULL,
                               email VARCHAR(20) NOT NULL
);
INSERT INTO users VALUES (1, 'Alex', 'Popov', 'USA', 'email@mail999.com');
INSERT INTO users VALUES (2, 'Igor', 'Pov','PL', 'email@mail998.com');
INSERT INTO users VALUES (3, 'Mat', 'Damon','USA', 'email@mail997.com');
INSERT INTO users VALUES (4, 'Steave', 'Jobs','DE', 'email@mail996.com');
INSERT INTO users VALUES (5, 'Garry', 'Gogol','DE', 'email@mail995.com');
INSERT INTO users VALUES (6, 'Rally', 'Ral','USA', 'email@mail994.com');
INSERT INTO users VALUES (7, 'Sov', 'Stasii','PL', 'email@mail99.com');
INSERT INTO users VALUES (8, 'Danny', 'Bod','USA', 'email@mail949.com');
INSERT INTO users VALUES (9, 'Maik', 'Hov','PL', 'email@mail9955.com');
INSERT INTO users VALUES (10, 'Hobc', 'Entiny','DE', 'email@mail49.com');
INSERT INTO users VALUES (11, 'Stan', 'Jeramy','USA', 'email@mail299.com');
INSERT INTO users VALUES (12, 'Gogolen', 'Hgfg','DE', 'email@mail939.com');
INSERT INTO users VALUES (13, 'Wiktorya', 'Alo','PL', 'email@mail923329.com');
INSERT INTO users VALUES (14, 'Mis', 'Jafo','USA', 'email@mail992349.com');
INSERT INTO users VALUES (15, 'Ritta', 'Skitter','PL', 'email@mail.com');
INSERT INTO users VALUES (16, 'Janny', 'Hobbu','USA', 'email@madsfil999.com');
INSERT INTO users VALUES (17, 'Ron', 'Wisley','DE', 'email@maisdfl999.com');
INSERT INTO users VALUES (18, 'Harry', 'Potter','USA', 'emaiadl@mail999.com');
INSERT INTO users VALUES (19, 'Germiona', 'Greindger','USA', 'email@mafffil999.com');
INSERT INTO users VALUES (20, 'Hagrid', 'Bro','PL', 'email@mal999.com');