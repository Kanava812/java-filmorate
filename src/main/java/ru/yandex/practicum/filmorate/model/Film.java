package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
     private long id;

     @NotBlank(message = "Не может быть пустым.")
     private String name;

     @Size(min = 1, max = 200, message = "Не более 200 символов.")
     private String description;

     @NotNull(message = "Не может быть null.")
     private LocalDate releaseDate;

     @Positive(message = "Длительность фильма должна быть положительным числом.")
     private int duration;

     private Set<Long> likes = new HashSet<>();
}

