INSERT INTO films (title, description, release_date, duration)
VALUES ('newTitle', 'newDescription', CURRENT_DATE - interval '30 year', 999)
ON CONFLICT (title, description, release_date, duration) DO NOTHING;

INSERT INTO ratings_mpa (rating_mpa)
VALUES ('newRatingMPA')
ON CONFLICT (rating_mpa) DO NOTHING;

INSERT INTO film_rating_mpa (film_id, rating_mpa_id)
VALUES ((SELECT f.film_id
			FROM films AS f
			WHERE f.title = 'newTitle' AND 
				  f.description = 'newDescription' AND 
				  f.release_date = CURRENT_DATE - interval '30 year' AND 
				  f.duration = 999), 
		(SELECT rm.rating_mpa_id
					FROM ratings_mpa AS rm
					WHERE rm.rating_mpa = 'newRatingMPA'))
ON CONFLICT (film_id, rating_mpa_id) DO NOTHING;
				
INSERT INTO genres (genre)
VALUES ('newGenre')
ON CONFLICT (genre) DO NOTHING;

INSERT INTO film_genre (film_id, genre_id)
VALUES ((SELECT f.film_id
			FROM films AS f
			WHERE f.title = 'newTitle' AND 
				  f.description = 'newDescription' AND 
				  f.release_date = CURRENT_DATE - interval '30 year' AND 
				  f.duration = 999), 
		(SELECT g.genre_id
			FROM genres AS g
			WHERE g.genre = 'newGenre'))
ON CONFLICT (film_id, genre_id) DO NOTHING;
				
INSERT INTO film_likes (film_id, user_id)
VALUES ((SELECT f.film_id
			FROM films AS f
			WHERE f.title = 'newTitle' AND 
				  f.description = 'newDescription' AND 
				  f.release_date = CURRENT_DATE - interval '30 year' AND 
				  f.duration = 999), 
		(SELECT u.user_id
			FROM users AS u
			WHERE u.user_id = userId))
ON CONFLICT (film_id, user_id) DO NOTHING;