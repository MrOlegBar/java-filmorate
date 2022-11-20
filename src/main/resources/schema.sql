CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title varchar NOT NULL,
    description varchar(200),
    release_date date NOT NULL,
    duration int NOT NULL CHECK (duration > 0),
	UNIQUE (title, description, release_date, duration)
);
CREATE UNIQUE INDEX IF NOT EXISTS film_id_idx ON films (film_id);

CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre varchar NOT NULL CHECK (genre != '') UNIQUE
);
CREATE UNIQUE INDEX IF NOT EXISTS genre_id_idx ON genres (genre_id);

CREATE TABLE IF NOT EXISTS film_genre (
    film_genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres (genre_id) ON DELETE CASCADE,
	UNIQUE (film_id, genre_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS film_genre_id_idx ON film_genre (film_genre_id);

CREATE TABLE IF NOT EXISTS ratings_MPA (
    rating_MPA_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    rating_MPA varchar NOT NULL CHECK (rating_MPA != '') UNIQUE
);
CREATE UNIQUE INDEX IF NOT EXISTS rating_MPA_id_idx ON ratings_MPA (rating_MPA_id);

CREATE TABLE IF NOT EXISTS film_rating_MPA (
    film_rating_MPA_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    rating_MPA_id INTEGER REFERENCES ratings_MPA (rating_MPA_id) ON DELETE CASCADE,
	UNIQUE (film_id, rating_MPA_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS film_rating_MPA_id_idx ON film_rating_MPA (film_rating_MPA_id);

CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar NOT NULL CHECK (login != '') UNIQUE,
    name varchar CHECK (name != ''),
    email varchar NOT NULL CHECK (email LIKE '%@%.%') UNIQUE,
    birthday date NOT NULL CHECK (birthday < CURRENT_DATE),
	UNIQUE (login, name, email, birthday)
);
CREATE UNIQUE INDEX IF NOT EXISTS user_id_idx ON users (user_id);

CREATE TABLE IF NOT EXISTS film_likes (
    film_like_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
	CONSTRAINT unique_film_id_user_id UNIQUE (film_id, user_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS film_like_id_idx ON film_likes (film_like_id);

CREATE TABLE IF NOT EXISTS friendship_status (
    friendship_status_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status varchar CHECK (status IN ('Неподтверждённая', 'Подтверждённая')) UNIQUE
);       
CREATE UNIQUE INDEX IF NOT EXISTS friendship_status_id_idx ON friendship_status (friendship_status_id);

CREATE TABLE IF NOT EXISTS user_friends (
    user_friend_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friend_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    friendship_status_id INTEGER REFERENCES friendship_status (friendship_status_id) ON DELETE CASCADE,
	UNIQUE (user_id, friend_id, friendship_status_id),
	UNIQUE (user_id, friend_id)
);
CREATE UNIQUE INDEX IF NOT EXISTS user_friend_id_idx ON user_friends (user_friend_id);