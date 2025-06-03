package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres ORDER BY genre_id";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, genreMapper, id).stream().findFirst();
    }

    @Override
    public Map<Long, Genre> getGenresByIds(Collection<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        String sql = "SELECT * FROM genres WHERE genre_id IN (:ids)";
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        List<Genre> result = namedParameterJdbcTemplate.query(sql, parameters, genreMapper);
        return result.stream()
                .collect(Collectors.toMap(Genre::getId, Function.identity()));
    }
}

