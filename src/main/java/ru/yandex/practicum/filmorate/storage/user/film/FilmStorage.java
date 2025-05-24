package ru.yandex.practicum.filmorate.storage.user.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    void delete(long id);

    Film getById(long id);
}
