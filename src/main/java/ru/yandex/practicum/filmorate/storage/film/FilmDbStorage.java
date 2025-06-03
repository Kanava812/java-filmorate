package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.MPA.MpaStorage;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Primary
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmExtractor filmExtractor;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Override
    public Film create(Film film) {
        mpaStorage.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> new NotFoundException("MPA с ID " + film.getMpa().getId() + " не найден"));
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getGenreById(genre.getId())
                        .orElseThrow(() -> new NotFoundException("Жанр с ID " + genre.getId() + " не найден"));
            }
        }
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES (?, ?, ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, film.getName());
            ps.setObject(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setObject(4, film.getDuration());
            ps.setLong(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            film.setId(id);
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }

        if (film.getGenres() != null) {
            String genreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?);";

            jdbcTemplate.batchUpdate(genreSql,
                    film.getGenres().stream()
                            .distinct()
                            .map(genre -> new Object[]{id, genre.getId()})
                            .collect(Collectors.toList())
            );
        }
        return film;
    }

    @Override
    public List<Film> findAll() {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id";
        return jdbcTemplate.query(sql, filmExtractor);
    }

    @Override
    public Film update(Film film) {
        if (getById(film.getId()) == null) {
            throw new NotFoundException("Фильм с id=" + film.getId() + " не найден");
        }
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? WHERE id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Film getById(long id) {
        String sql = "SELECT f.*, r.mpa_id, r.name AS mpa_name, " +
                "g.genre_id, g.name AS genre_name, " +
                "FROM films f " +
                "LEFT JOIN mpa_ratings r ON r.mpa_id = f.mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                "WHERE f.id = ?";

        List<Film> films = jdbcTemplate.query(sql, filmExtractor, id);
        return films.stream().findFirst().orElse(null);
    }

    @Override
    public void delete(long filmId) {
        final String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
