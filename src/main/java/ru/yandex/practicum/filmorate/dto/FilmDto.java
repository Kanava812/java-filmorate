package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotation.NotBeforeDate;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDto {
    @Positive
    private Long id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(min = 1, max = 200, message = "Не более 200 символов.")
    private String description;

    @NotNull(message = "Не может быть null.")
    @NotBeforeDate(message = "Дата выхода должна быть не раньше 28 декабря 1895 года.")
    private LocalDate releaseDate;

    @Positive(message = "Длительность фильма должна быть положительным числом")
    private Integer duration;
}

