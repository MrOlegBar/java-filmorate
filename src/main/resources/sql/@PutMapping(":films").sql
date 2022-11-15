UPDATE films
SET title = 'newTitle',
    description = 'newDescription',
    release_date = 'newRelease_date',
    duration = newDuration
WHERE film_id = (SELECT f.film_id
					FROM films AS f
					WHERE f.film_id = filmId);

INSERT INTO ratings_mpa (rating_mpa)
VALUES ('newRatingMPA')
ON CONFLICT (rating_mpa) DO NOTHING;

UPDATE film_rating_mpa
SET rating_mpa_id = (SELECT rm.rating_mpa_id
						FROM ratings_mpa AS rm
						WHERE rm.rating_mpa = 'newRatingMPA')
WHERE film_id = filmId;

INSERT INTO genres (genre)
VALUES ('newGenre')
ON CONFLICT (genre) DO NOTHING;

UPDATE film_genre
SET genre_id = (SELECT g.genre_id
					FROM genres AS g
					WHERE g.genre = 'newGenre')
WHERE film_id = filmId;

UPDATE film_likes
SET user_id (SELECT u.user_id
                FROM users AS u
                WHERE u.user_id = userId)
WHERE film_id = filmId;