package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @NonNull
    private Long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
}
