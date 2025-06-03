package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
@Primary
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public List<User> findAll() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, userMapper);
    }

    public User create(User user) {
        String query = "INSERT INTO users (name, login, email, birthday) VALUES (?, ?, ?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setObject(1, user.getName());
            ps.setObject(2, user.getLogin());
            ps.setObject(3, user.getEmail());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        Long id = keyHolder.getKeyAs(Long.class);
        if (id != null) {
            user.setId(id);
        } else {
            throw new RuntimeException("Не удалось сохранить данные");
        }
        log.info("Создан новый пользователь с id {} ", user.getId());
        return user;
    }

    @Override
    public void delete(long userId) {
        final String sql = "delete from users where id = ?";
        jdbcTemplate.update(sql, userId);
        log.info("Пользователь с id {} удален.", userId);
    }

    public User getById(long id) {
        String query = "SELECT * FROM users WHERE id = ?;";
        try {
            return jdbcTemplate.queryForObject(query, userMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с Id " + id + " не найден");
        }
    }

    public User update(User user) {
        if (getById(user.getId()) == null) {
            throw new NotFoundException("Пользователь с id=" + user.getId() + " не найден");
        }
        String query = "UPDATE users SET name = ?, login = ?, email = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(query, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        log.info("Пользователь с id {} обновлен.", user.getId());
        return user;
    }
}


