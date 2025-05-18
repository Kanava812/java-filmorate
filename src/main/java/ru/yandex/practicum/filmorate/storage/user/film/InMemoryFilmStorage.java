package ru.yandex.practicum.filmorate.storage.user.film;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    LocalDate firstFilmDate = LocalDate.of(1895, 12, 28);

    @Override
    public Collection<Film> findAll() {
        log.info("Список всех фильмов получен: {}", films.values());
        return films.values();
    }

    @Override
    public Film create(@Valid @RequestBody Film film) {
        log.info("Начато создание фильма.");
        film.setId(getNextId());
        if (film.getReleaseDate().isBefore(firstFilmDate)) {
            throw new ValidationException("Дата релиза не раньше 28.12.1895.");
        } else {
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film);
            return film;
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма c id {}", newFilm.getId());
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.info("Фильм с указанным id найден: {}", oldFilm);
            if (newFilm.getReleaseDate().isBefore(firstFilmDate)) {
                throw new ValidationException("Дата релиза не раньше 28.12.1895.");
            } else {
                oldFilm.setName(newFilm.getName());
                oldFilm.setDescription(newFilm.getDescription());
                oldFilm.setDuration(newFilm.getDuration());
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
                log.info("Фильм обновлен: {}", oldFilm);
                return oldFilm;
            }
        }
        throw new NotFoundException("Фильм с указанным id не существует.");
    }

    @Override
    public void delete(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм с указанным id не существует.");
        }
        films.remove(id);
    }

    @Override
    public Film getById(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        }
        throw new NotFoundException("Фильм с указанным id не существует.");
    }
}
