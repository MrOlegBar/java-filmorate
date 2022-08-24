package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new TreeMap<>();
    private final LocalDate dateCheck = LocalDate.of(1895, 12, 28);

    public void save(Film film) {
        film.setId(idGeneration());
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> findAll() {
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
    public Film update(Film film) throws ValidationException {
        releaseDateCheck(film.getReleaseDate());

        if (!films.containsKey(film.getId()) || film.getId() < 1) {
            ValidationException e = new ValidationException("Film с id = " + film.getId() + " не существует");
            log.debug("Валидация не пройдена", e);
            throw e;
        }

        save(film);

        log.debug("Пользователь: {} сохранен.", film);
        return film;
    }

    @Override
    public Film getFilmById(int filmId) {
        Film film = null;
        for (Film filmFromMap : films.values()) {
            if (filmId == filmFromMap.getId()) {
                film = filmFromMap;
            }
        }
        return film;
    }

    private int idGeneration() {
        int id = 1;
        while (films.containsKey(id)) {
            id++;
        }
        return id;
    }

    private void releaseDateCheck(LocalDate releaseDate) {
        if (releaseDate.isBefore(dateCheck)) {
            ValidationException e = new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
            log.debug("Валидация не пройдена", e);
            throw e;
        }
    }
}