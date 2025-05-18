package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@Data
public class User {
    private long id;

    @Email(message = "Адрес должен содержать символ @.")
    @NotBlank
    private String email;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы.")
    @NotNull(message = "Не может быть null.")
    private String login;

    private String name;

    @PastOrPresent(message = "ДР не может быть в будущем.")
    private LocalDate birthday;
}
