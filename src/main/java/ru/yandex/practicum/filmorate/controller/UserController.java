package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Получен запрос на поиск всех пользователей.");
        return userService.findAll();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание нового пользователя  {} ", user.getName());
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя с id {} ", newUser.getId());
        return userService.update(newUser);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable @Positive(message = "ID должен быть положительным числом") long id) {
        log.info("Запрос на поиск пользователя по id {}.", id);
        return userService.getById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") @Positive(message = "ID должен быть положительным числом") long id,
                          @PathVariable("friendId") @Positive(message = "ID должен быть положительным числом") long
                                  friendId) {
        log.info("Запрос дружбы от пользователя с id {} пользователю с id {}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") @Positive(message = "ID должен быть положительным числом") long id,
                             @PathVariable("friendId") @Positive(message = "ID должен быть положительным числом") long
                                     friendId) {
        log.info("Запрос на удаление пользователя с id {} из списка друзей из списка друзей пользователя с id{}.",
                friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@PathVariable long id) {
        log.info("Запрос на получение списка друзей пользователя с id {}.", id);
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable("id") @Positive(message = "ID должен быть положительным " +
            "числом") long id, @PathVariable("otherId") @Positive(message = "ID должен быть положительным числом") long
                                                     otherId) {
        log.info("Запрос на получение списка общих друзей пользователей с id {} и id {}.", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
