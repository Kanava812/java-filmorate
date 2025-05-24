package ru.yandex.practicum.filmorate.storage.user;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.info("Список всех пользователей получен: {}", users.values());
        return users.values();
    }

    @Override
    public User create(User user) {
        log.info("Начато создание нового пользователя.");
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Новый пользователь добавлен: {}", user);
        return user;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @Override
    public User update(User newUser) {
        log.info("Получен запрос на обновление пользователя с id {}", newUser.getId());
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            log.info("Пользователь найден: {}", oldUser);
            if (newUser.getName() == null || newUser.getName().isBlank()) {
                oldUser.setName(newUser.getLogin());
            } else {
                oldUser.setName(newUser.getName());
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Пользователь обновлен: {}", oldUser);
            return oldUser;
        }
        throw new NotFoundException("Пользователя с указанным id не существует.");
    }

    @Override
    public void delete(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с указанным id не существует.");
        }
        users.remove(id);
    }

    @Override
    public User getById(long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }
        throw new NotFoundException("Пользователя с указанным id не существует.");
    }
}
