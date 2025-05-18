package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.user.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmTest {
    private Validator validator;
    private Film film;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
        film.setId(1);
        film.setName("Робокоп");
        film.setDescription("Мужик умер, а его всё равно заставили ходить на работу.");
        film.setReleaseDate(LocalDate.of(1987, 7, 17));
        film.setDuration(102);
    }

    @Test
    public void nameTest() {
        film.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals("Не может быть пустым.", violations.iterator().next().getMessage());
    }

    @Test
    public void descriptionTest() {
        film.setDescription(film.getDescription().repeat(200));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals("Не более 200 символов.", violations.iterator().next().getMessage());
    }

    @Test
    public void releaseDateTest() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 20));
        FilmController fc = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        ValidationException e = assertThrows(ValidationException.class, () -> fc.create(film));
        Assertions.assertEquals("Дата релиза не раньше 28.12.1895.", e.getMessage());
    }

    @Test
    public void durationTest() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals("Длительность фильма должна быть положительным числом.",
                violations.iterator().next().getMessage());
    }
}