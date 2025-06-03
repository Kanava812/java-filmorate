package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Optional<Genre> getGenreById(Long id);

    Map<Long, Genre> getGenresByIds(Collection<Long> ids);
}
