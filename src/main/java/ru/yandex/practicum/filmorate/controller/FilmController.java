package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.like.LikeDbStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
@RestController
@Validated
public class FilmController {

    private final FilmService filmService;
    private final LikeDbStorage likeDbStorage;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Получен запрос на поиск всех фильмов.");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма {}.", film.getName());
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма {}.", newFilm.getName());
        return filmService.update(newFilm);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        log.info("Запрос на поиск фильма по id {}.", id);
        return filmService.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable("id") @Positive(message = "ID должен быть положительным числом") long id,
                        @PathVariable("userId") @Positive(message = "ID должен быть положительным числом") long userId) {
        log.info("Запрос на оценку фильма с id {} пользователем с id {}.", id, userId);
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") @Positive(message = "ID должен быть положительным числом") long id,
                           @PathVariable("userId") @Positive(message = "ID должен быть положительным числом") long
                                   userId) {
        log.info("Запрос на удаление лайка у фильма с id {} пользователем с id {} .", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на {} самых популярных фильмов.", count);
        return likeDbStorage.getPopularFilm(count);
    }
}
