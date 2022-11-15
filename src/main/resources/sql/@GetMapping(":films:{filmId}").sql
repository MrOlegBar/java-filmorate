SELECT f.title,
        f.description,
        f.release_date,
        f.duration,
        array_agg(DISTINCT g.genre) AS genre,
        rm.rating_mpa,
        array_agg(DISTINCT fl.user_id) AS film_like
FROM films AS f
LEFT JOIN film_genre AS fg ON f.film_id=fg.film_id
LEFT JOIN genres AS g ON fg.genre_id=g.genre_id
LEFT JOIN film_rating_mpa AS frm ON f.film_id=frm.film_id
LEFT JOIN ratings_mpa AS rm ON frm.rating_MPA_id=rm.rating_MPA_id
LEFT JOIN film_likes AS fl ON f.film_id=fl.film_id
WHERE f.film_id = 1
GROUP BY f.title, f.description, f.release_date, f.duration, rm.rating_mpa;