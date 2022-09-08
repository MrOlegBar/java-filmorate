# data base diagram

![data base diagram](src/main/images/Data Base Diagram.png)
# FilmController
1. @GetMapping("/films")
- SELECT *
- FROM film AS f
- LEFT INNER JOIN film_likes AS fl ON f.film_id=fl.film_id
- LEFT INNER JOIN genre AS g ON f.genre_id=g.genre_id
- LEFT INNER JOIN ratingMPA AS r ON f.ratingMPA_id=r.ratingMPA_id;
2. @GetMapping("/films/{filmId}")
- SELECT *
- FROM film AS f
- LEFT INNER JOIN film_likes AS fl ON f.film_id=fl.film_id
- LEFT INNER JOIN genre AS g ON f.genre_id=g.genre_id
- LEFT INNER JOIN ratingMPA AS r ON f.ratingMPA_id=r.ratingMPA_id
- WHERE film_id = filmId;
3. @GetMapping("/films/popular?count={countFilms}")
- SELECT *
- FROM film AS f
- LEFT INNER JOIN film_likes AS fl ON f.film_id=fl.film_id
- LEFT INNER JOIN genre AS g ON f.genre_id=g.genre_id
- LEFT INNER JOIN ratingMPA AS r ON f.ratingMPA_id=r.ratingMPA_id
- WHERE 
- LIMIT countFilms
# UserController
1. SELECT *
- FROM user AS u
- LEFT INNER JOIN 