package ru.yandex.practicum.filmorate.storage.impl.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.film.RatingMpa;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
public class RatingMpaDbStorageTest {
    @Autowired
    RatingMpaDbStorage ratingMpaDbStorage;

    @Test
    void contextLoads() {
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getAllRatingsMpa() {
        Collection<RatingMpa> testRatingsMpa = new ArrayList<>();

        testRatingsMpa.add(RatingMpa.builder().id(1).name("G").build());
        testRatingsMpa.add(RatingMpa.builder().id(2).name("PG").build());
        testRatingsMpa.add(RatingMpa.builder().id(3).name("PG-13").build());
        testRatingsMpa.add(RatingMpa.builder().id(4).name("R").build());
        testRatingsMpa.add(RatingMpa.builder().id(5).name("NC-17").build());

        Collection<RatingMpa> foundRatingsMpa = ratingMpaDbStorage.getAllRatingsMpa();

        assertNotNull(foundRatingsMpa);
        assertEquals(testRatingsMpa, foundRatingsMpa);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void getRatingMpaById() {
        RatingMpa testRatingMpa = RatingMpa.builder().id(1).name("G").build();

        RatingMpa foundRatingMpa = ratingMpaDbStorage.getRatingMpaById(1);

        assertNotNull(foundRatingMpa);
        assertEquals(testRatingMpa, foundRatingMpa);
    }
}
