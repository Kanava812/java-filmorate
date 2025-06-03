package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final FriendStorage friendsStorage;

    public void addFriend(long id, long friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        friendsStorage.addFriend(id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", user.getName(), friend.getName());
    }

    public void deleteFriend(long id, long friendId) {
        User user = getById(id);
        User friend = getById(friendId);
        friendsStorage.deleteFriend(id, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", user.getName(), friend.getName());
    }

    public Collection<User> getAllFriends(long id) {
        User user = getById(id);
        return friendsStorage.getUserFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        User user1 = getById(id);
        User user2 = getById(otherId);
        return friendsStorage.getCommonFriends(id, otherId);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void delete(long id) {
        userStorage.delete(id);
    }

    public User getById(long id) {
        return userStorage.getById(id);
    }
}
