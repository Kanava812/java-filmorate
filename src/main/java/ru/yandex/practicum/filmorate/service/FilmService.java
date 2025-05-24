package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.user.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void putLike(long id, long userId) {
        Film film = filmStorage.getById(id);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь уже оценил фильм.");
        }
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователя с указанным id не существует.");
        }
        film.getLikes().add(userId);
        filmStorage.update(film);
    }

    public void deleteLike(long id, long userId) {
        Film film = filmStorage.getById(id);
        if (userStorage.getById(userId) == null) {
            throw new NotFoundException("Пользователя с указанным id не существует.");
        }
        film.getLikes().remove(userId);
        filmStorage.update(film);
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .toList();
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public void delete(long id) {
        filmStorage.delete(id);
    }

    public Film getById(long id) {
        return filmStorage.getById(id);
    }
}
