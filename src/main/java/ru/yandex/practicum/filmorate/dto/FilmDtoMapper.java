package ru.yandex.practicum.filmorate.dto;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmDtoMapper implements RowMapper<FilmDto> {

    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(rs.getInt("duration"))
                .build();
    }
}
