MERGE INTO ratings_MPA (rating_MPA)
KEY (rating_MPA)
VALUES ('G'),
	('PG'),
	('PG-13'),
	('R'),
	('NC-17');

MERGE INTO genres (genre)
KEY (genre)
VALUES ('Комедия'),
        ('Драма'),
        ('Мультфильм'),
        ('Триллер'),
        ('Документальный'),
        ('Боевик');