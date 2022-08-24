package ru.yandex.practicum.filmorate.comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;

@Component
public class LikesFilmComparator implements Comparator<Long> {
    FilmStorage filmStorage;

    @Autowired
    public LikesFilmComparator(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public int compare(Long userId1, Long userId2) {
        long id1 = userId1;
        long id2 = userId2;

        Film film1 = filmStorage.getFilmById((int) id1);
        Film film2 = filmStorage.getFilmById((int) id2);

        return Integer.compare(film1.getLikes().size(), film2.getLikes().size());
    }
}