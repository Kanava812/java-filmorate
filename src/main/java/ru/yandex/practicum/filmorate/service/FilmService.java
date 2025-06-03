package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.MPA.MpaStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final LikeStorage likeStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    public void putLike(Long filmId, Long userId) {
        likeStorage.addLike(filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public List<FilmDto> getPopularFilm(int count) {
        return likeStorage.getPopularFilm(count);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        mpaStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с ID " + film.getMpa().getId() + " не найден"));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getGenreById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр с ID " + genre.getId() + " не найден"));
            }
        }
        Set<Long> genreIdsToCheck = film.getGenres() != null ?
                film.getGenres().stream().map(Genre::getId).collect(Collectors.toSet()) :
                Collections.emptySet();
        Map<Long, Genre> existingGenresMap = genreStorage.getGenresByIds(genreIdsToCheck);
        if (existingGenresMap.size() < genreIdsToCheck.size()) {
            Set<Long> missingGenreIds = genreIdsToCheck.stream()
                    .filter(id -> !existingGenresMap.containsKey(id))
                    .collect(Collectors.toSet());
            throw new NotFoundException("Отсутствуют жанры с ID: " + missingGenreIds);
        }

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
