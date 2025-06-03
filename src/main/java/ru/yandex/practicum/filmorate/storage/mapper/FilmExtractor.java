package ru.yandex.practicum.filmorate.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FilmExtractor implements ResultSetExtractor<List<Film>> {

    private final MpaMapper mpaMapper;

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Film> filmMap = new HashMap<>();

        while (rs.next()) {
            long currentFilmId = rs.getLong("id");
            Film film = filmMap.computeIfAbsent(currentFilmId, id -> {
                try {
                    return Film.builder()
                            .id(currentFilmId)
                            .name(rs.getString("name"))
                            .description(rs.getString("description"))
                            .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                            .duration(rs.getInt("duration"))
                            .mpa(getMpaFromResultSet(rs))
                            .build();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (rs.getLong("genre_id") != 0 && !rs.wasNull()) {
                String genreName = rs.getString("genre_name");
                if (genreName != null) {
                    film.getGenres().add(new Genre(rs.getLong("genre_id"), genreName));
                }
            }
        }
        return new ArrayList<>(filmMap.values());
    }

    private Mpa getMpaFromResultSet(ResultSet rs) throws SQLException {
        return mpaMapper.mapRow(rs, 0);
    }
}


