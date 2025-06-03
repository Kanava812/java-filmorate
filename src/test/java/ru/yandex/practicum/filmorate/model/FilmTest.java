package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    void releaseDateTest() {
        Mpa mpa = new Mpa(1L, "G");
        Set<Genre> genres = new HashSet<>(1);
        Film film = new Film(1L, "Name", "Description", LocalDate.of(1895, 12,
                27), 100, mpa, genres);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size(), "Должна быть одна ошибка валидации");
        assertEquals("Дата выхода должна быть не раньше 28 декабря 1895 года.", violations.iterator().next()
                .getMessage());
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