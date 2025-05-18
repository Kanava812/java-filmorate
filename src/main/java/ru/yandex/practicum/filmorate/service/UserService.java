package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriend(long id, long friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);
        if (user.getFriends().contains(friendId) && friend.getFriends().contains(id)) {
            throw new ValidationException("Пользователи уже дружат.");
        }
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        userStorage.update(user);
        userStorage.update(friend);
    }

    public void deleteFriend(long id, long friendId) {
        User user = userStorage.getById(id);
        User friend = userStorage.getById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);

        userStorage.update(user);
        userStorage.update(friend);
    }

    public Collection<User> getAllFriends(long id) {
        User user = userStorage.getById(id);
        Collection<User> friends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            userStorage.getById(friendId);
            friends.add(userStorage.getById(friendId));
        }
        return friends;
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        Set<Long> userFriends = new HashSet<>(userStorage.getById(id).getFriends());
        Set<Long> otherUserFriends = new HashSet<>(userStorage.getById(otherId).getFriends());
        userFriends.retainAll(otherUserFriends);
        return userFriends.stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
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
