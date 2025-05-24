# java-filmorate
Template repository for Filmorate project.
## ER-диаграмма

![FILMORATEDB1](https://github.com/user-attachments/assets/23de5f18-f9a9-4fe7-8aa0-01384b8d05b0)

## Описание диаграммы

### <ins>USERS</ins>

- **ID** - идентификатор пользователя (int)

- **EMAIL** - почта пользователя (varchar)

- **LOGIN** - логин пользователя (varchar)

- **NAME** - имя пользователя (varchar)

- **BIRTHDAY** - дата рождения пользователя (date)

### <ins>FRIENDS</ins>

- **USER ID** - идентификатор пользователя, отправившего запрос на добавление в друзья (int)

- **FRIEND ID** - идентификатор пользователя, которому поступил запрос (int)

- **STATUS** - состояние запроса (boolean)

>**true** - принят\
**false** - ожидание подтверждения/отклонен

### <ins>FILMS</ins>

- **ID** - идентификатор фильма (int)

- **NAME** - название фильма (varchar)

- **DESCRIPTION** - описание фильма (varchar)

- **RELEASE DATE** - дата релиза фильма (date)

- **DURATION** - продолжительность фильма в минутах (int)

- **MPA ID** - идентификатор рейтинга фильма (int)

- ### <ins>MPA</ins>

- **ID** - идентификатор рейтинга (int)

- **NAME** - название рейтинга (varchar)

> **1 - G** - нет возрастных ограничений\
**2 - PG** - рекомендуют смотреть с родителями\
**3 - PG-13** - до 13 лет просмотр не желателен\
**4 - R** - до 17 лет только в присутствии взрослого\
**5 - NC-17** - до 18 лет просмотр запрещен


### <ins>GENRES</ins>

- **GENRE ID** - идентификатор жанра (int)

- **NAME** - название жанра (varchar)

> **1** - Комедия\
**2** - Драма\
**3** - Мультфильм\
**4** - Триллер\
**5** - Документальный\
**6** - Боевик

### <ins>FILM_GENRE</ins>

- **FILM ID** - идентификатор фильма (int)

- **GENRE ID** - идентификатор жанра (int)

### <ins>LIKES</ins>

- **FILM ID** - идентификатор фильма, которому поставили лайк (int)

- **USER ID** - идентификатор пользователя, который поставил лайк (int)


## Примеры работы с базой
SELECT * FROM films;<br />
Получить все поля из таблицы FILMS<br />
<br />
SELECT films.name,<br />
films.description,<br />
genres.name <br />
FROM films<br />
JOIN film_genre ON films.id = film_genre.film_id<br />
JOIN genres ON film_genre.genre_id = genres.id;<br />
Получить названия, описания фильмов и жанры<br />
<br />
SELECT genres.name,<br />
COUNT(Film_Genre.film_id) AS film_count<br />
FROM genres<br />
LEFT JOIN film_genre ON genres.id = film_genre.genre_id<br />
GROUP BY genres.id;<br />
Получить все жанры и количество фильмов каждого жанра<br />
<br />
SELECT users.name,<br />
users.email,<br />
FROM users<br />
WHERE birthday>='01.01.2012;<br />
Получить имя и почту пользователей, родившихся в 2012г и позже<br />
