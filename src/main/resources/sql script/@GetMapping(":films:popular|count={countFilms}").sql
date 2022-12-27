CREATE OR REPLACE VIEW FILMS_RATING_MPA_VIEW AS
SELECT f.film_id,
       f.TITLE,
       f.DESCRIPTION,
       f.RELEASE_DATE,
       f.DURATION,
       f.RATING_MPA_ID,
       rm.RATING_MPA,
       f.rate
FROM FILMS f
         LEFT OUTER JOIN RATINGS_MPA rm ON f.RATING_MPA_ID = rm.RATING_MPA_ID;

SELECT * FROM FILMS_RATING_MPA_VIEW ORDER BY RATE DESC LIMIT ?;