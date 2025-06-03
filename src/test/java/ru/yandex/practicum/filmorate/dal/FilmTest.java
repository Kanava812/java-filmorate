package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dto.FilmDtoMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MPA.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.mapper.FilmExtractor;
import ru.yandex.practicum.filmorate.storage.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.storage.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.storage.mapper.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmDtoMapper.class, GenreMapper.class, GenreDbStorage.class, MpaDbStorage.class,
        MpaMapper.class, UserDbStorage.class, UserMapper.class, FilmExtractor.class})
class FilmTest {
    private final FilmDbStorage filmDbStorage;

    @Test
    public void createUpdateDeleteFilmTest() {
        Film film = Film.builder()
                .name("Film")
                .description("Description")
                .releaseDate(LocalDate.of(2025, 5, 5))
                .duration(120)
                .mpa(new Mpa(1L, "G"))
                .genres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")))
                .build();
        filmDbStorage.create(film);
        Film film1 = filmDbStorage.getById(1L);
        assertEquals(film, film1, "Фильмы должны быть одинаковые.");

        Film film2 = Film.builder()
                .id(1L)
                .name("Film1")
                .description("Description1")
                .releaseDate(LocalDate.of(2025, 5, 15))
                .duration(150)
                .mpa(new Mpa(1L, "G"))
                .genres(Set.of(new Genre(1L, "Комедия"), new Genre(2L, "Драма")))
                .build();
        film = filmDbStorage.update(film2);
        assertNotEquals(film, film1, "Фильмы должны быть разные.");

        filmDbStorage.delete(1L);
        assertEquals(filmDbStorage.getById(1), null, "Фильм должен быть удален.");
    }
}