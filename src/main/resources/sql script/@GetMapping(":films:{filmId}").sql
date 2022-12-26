CREATE VIEW IF NOT EXISTS films_ratings_MPA_view AS
SELECT f.film_id,
       f.TITLE,
       f.DESCRIPTION,
       f.RELEASE_DATE,
       f.DURATION,
       f.RATING_MPA_ID,
       rm.RATING_MPA,
       fg.GENRE_ID,
       g.GENRE
FROM FILMS f
         INNER JOIN RATINGS_MPA rm ON f.RATING_MPA_ID = rm.RATING_MPA_ID
         INNER JOIN FILM_GENRE fg ON f.FILM_ID = fg.FILM_ID
         INNER JOIN GENRES g ON fg.GENRE_ID = g.GENRE_ID;

SELECT * FROM films_ratings_MPA_view WHERE FILM_ID = 1;