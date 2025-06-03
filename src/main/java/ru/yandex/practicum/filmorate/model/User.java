package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@Data
public class User {
    private long id;

    private String name;

    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелы.")
    @NotNull(message = "Не может быть null.")
    private String login;

    @Email(message = "Адрес должен содержать символ @.")
    @NotBlank
    private String email;

    @PastOrPresent(message = "ДР не может быть в будущем.")
    private LocalDate birthday;


    public User(Long id, String name, String login, String email, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
