package ru.yandex.practicum.filmorate.storage.MPA;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;


    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT mpa_id, name AS mpa_name FROM mpa_ratings ORDER BY mpa_id";
        return jdbcTemplate.query(sql, mpaMapper);
    }

    @Override
    public Optional<Mpa> getMpaById(Long id) {
        String sql = "SELECT mpa_id, name AS mpa_name FROM mpa_ratings WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, mpaMapper, id).stream().findFirst();
    }
}
