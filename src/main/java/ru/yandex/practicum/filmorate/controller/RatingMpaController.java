package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.RatingMpaNotFoundException;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

@RestController
@AllArgsConstructor
public class RatingMpaController {
    RatingMpaService ratingMpaService;

    @GetMapping(value = { "/mpa", "/mpa/{ratingMpaId}"})
    @ResponseBody
    public Object getRatingMpaS(@PathVariable(required = false) Integer ratingMpaId) throws RatingMpaNotFoundException {
        if (ratingMpaId != null) {
            return ratingMpaService.getRatingMpaById(ratingMpaId);
        } else {
            return ratingMpaService.getAllRatingsMpa();
        }
    }
}