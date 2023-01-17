package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Component("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmDao implements FilmDao {
    private final Map<Integer, Film> films = new TreeMap<>();
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);
    private static Integer globalId = 0;

    public void save(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @Override
    public Film create(Film film) throws ValidationException {
        releaseDateCheck(film.getReleaseDate());

        save(film);

        log.debug("Фильм: {} сохранен.", film);
        return film;
    }

    @Override
    public Film update(Film film) throws FilmNotFoundException {
        releaseDateCheck(film.getReleaseDate());

        if (!films.containsKey(film.getId()) || film.getId() < 1) {
            FilmNotFoundException e = new FilmNotFoundException("Film с id = " + film.getId() + " не существует");
            log.debug("Валидация не пройдена", e);
            throw e;
        }

        films.put(film.getId(), film);

        log.debug("Пользователь: {} сохранен.", film);
        return film;
    }

    @Override
    public Film getFilmById(int filmId) throws FilmNotFoundException {
        Film film = null;
        for (Film filmFromMap : films.values()) {
            if (filmId == filmFromMap.getId()) {
                film = filmFromMap;
            }
        }

        if (film == null) {
            FilmNotFoundException e = new FilmNotFoundException("Film с id = " + filmId + " не существует");
            log.debug("Валидация не пройдена", e);
            throw e;
        }
        return film;
    }

    private static Integer getNextId() {
        return ++globalId;
    }

    private void releaseDateCheck(LocalDate releaseDate) throws ValidationException {
        if (releaseDate.isBefore(dateCheck)) {
            ValidationException e = new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
            log.debug("Валидация не пройдена", e);
            throw e;
        }
    }
}