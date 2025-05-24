package ru.yandex.practicum.filmorate.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class UserTest {
    private Validator validator;
    private User user;

    @BeforeEach
    public void beforeEach() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        user = new User();
        user.setId(1);
        user.setName("name");
        user.setLogin("login");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    public void loginTest() {
        user.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        user.setLogin("lo gin");
        Set<ConstraintViolation<User>> violations1 = validator.validate(user);
        Assertions.assertFalse(violations1.isEmpty());
        Assertions.assertEquals("Логин не должен содержать пробелы.",
                violations1.iterator().next().getMessage());
    }

    @Test
    public void nameTest() {
        user.setName("");
        UserController uc = new UserController(new UserService(new InMemoryUserStorage()));
        Assertions.assertEquals(uc.create(user).getName(), user.getLogin(),
                "Если имя не задано,то будет использован логин;");
    }

    @Test
    public void emailTest() {
        user.setEmail("");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        user.setEmail("mail.ru");
        Set<ConstraintViolation<User>> violations1 = validator.validate(user);
        Assertions.assertFalse(violations1.isEmpty());
        Assertions.assertEquals("Адрес должен содержать символ @.", violations1.iterator().next().getMessage());
    }

    @Test
    public void birthdayTest() {
        user.setBirthday(LocalDate.of(2100, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
        Assertions.assertEquals("ДР не может быть в будущем.", violations.iterator().next().getMessage());
    }
}