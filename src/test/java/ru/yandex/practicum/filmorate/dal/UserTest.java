package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserMapper.class})
class UserTest {
    private final UserDbStorage userDbStorage;

    @Test
    public void createUpdateDeleteTest() {
        User user = User.builder()
                .email("www@mail.ru")
                .login("log")
                .name("Vova")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        userDbStorage.create(user);
        User user1 = userDbStorage.getById(1L);
        assertEquals(user, user1, "Пользователи должны совпадать");

        User user2 = User.builder()
                .id(1L)
                .email("www@mail.ru")
                .login("log")
                .name("Sasha")
                .birthday(LocalDate.of(1990, 9, 9))
                .build();
        user = userDbStorage.update(user2);
        assertNotEquals(user, user1, "Пользователи должны быть разные.");

        userDbStorage.delete(1L);
        Throwable exception = assertThrows(NotFoundException.class,
                () -> userDbStorage.getById(1L),
                "Удалённый пользователь не должен существовать");
        assertThat(exception.getMessage()).contains("Пользователь с Id 1 не найден"); // Проверка сообщения исключения
    }
}
