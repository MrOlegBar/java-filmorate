INSERT INTO film_likes (film_id, user_id)
VALUES ((SELECT f.film_id
			FROM films AS f
			WHERE f.film_id = filmId), 
	    (SELECT u.user_id
			FROM users AS u
			WHERE u.user_id = userId))
ON CONFLICT (film_id, user_id) DO NOTHING;