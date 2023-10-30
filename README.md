## java-explore-with-me
## Приложение в котором можно предложить какое-либо событие и собрать компанию для участия в нём.

## Cтек:
Java 11, Spring Boot, Spring Data, Lombok, Spring Jpa, PostgreSQL, Hibernate ORM, Docker. Реализован docker-compose.yml со всеми необходимыми параметрами.

<summary><h1> Основной сервис: </h1></summary>
Содержит всё необходимое для работы продукта.

### Схема базы данных основного сервиса:

![ewm-db](db-pictures%2Fewm_bd.png)

<details> 
<summary><h2> Эндпоинты </h2></summary>

### Публичные:
- >GET /compilations - Получение подборок событий
- >GET /compilations/{compId} - Получение подборки событий по его id
- >GET /categories - Получение категорий
- >GET /categories/{catId} - Получение информации о категории по её идентификатору
- >GET /events - Получение событий с возможностью фильтрации
- >GET /events/{id} - Получение подробной информации об опубликованном событии по его идентификатору
- >GET /comments - Получение комментария
- >GET /comments/users/{userId} - Получение комментариев от определённого пользователя
- >GET /comments/event/{eventId} - Получение комментариев к определённому событию
- >GET /rates/{eventId} - Получение рейтинга определённого события

### Для авторизованных пользователей:
- >POST /users/{userId}/events - Добавление нового события
- >GET /users/{userId}/events - Получение событий, добавленных текущим пользователем
- >GET /users/{userId}/events/{eventId} - Получение полной информации о событии добавленном текущим пользователем
- >PATCH /users/{userId}/events/{eventId} - Изменение события добавленного текущим пользователем
- >GET /users/{userId}/events/{eventId}/requests - Получение информации о запросах на участие в событии текущего пользователя
- >PATCH /users/{userId}/events/{eventId}/requests - Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
- >GET /users/{userId}/requests - Получение информации о заявках текущего пользователя на участие в чужих событиях
- >POST /users/{userId}/requests - Добавление запроса от текущего пользователя на участие в событии
- >PATCH /users/{userId}/requests/{requestId}/cancel - Отмена своего запроса на участие в событии
- >POST /users/{userId}/comments - Добавление комментария от текущего пользователя к событию
- >PATCH /users/{userId}/comments - Редактирование комментария добавленного текущим пользователем
- >DELETE /users/{userId}/comments - Удаление комментария добавленного текущим пользователем
- >POST /users/{userId}/rates - Добавление оценки от текущего пользователя событию
- >PATCH /users/{userId}/rates - Изменение оценки от текущего пользователя
- >DELETE /users/{userId}/rates - Удаление оценки от текущего пользователя

### Для администратора:
- >POST /admin/categories - Добавление новой категории
- >DELETE /admin/categories/{catId} - Удаление категории
- >PATCH /admin/categories/{catId} -  Изменение категории
- >POST /admin/compilations - Добавление новой подборки (подборка может не содержать событий)
- >DELETE /admin/compilations/{compId} - Удаление подборки
- >PATCH /admin/compilations/{compId} - Обновить информацию о подборке
- >GET /admin/events - Поиск событий
- >PATCH /admin/events/{eventId} - Редактирование данных события и его статуса (отклонение/публикация)
- >GET /admin/users - Получение информации о пользователях
- >POST /admin/users - Добавление нового пользователя
- >DELETE /admin/users/{userId} - Удаление пользователя
- >DELETE /admin/comments/{commentId} - удаление комментария
</details>

<summary><h1> Сервис статистики: </h1></summary>
Хранит количество просмотров.

### Схема базы данных сервиса статистики:

![stats-db](db-pictures%2Fstats-db.png)

<details> 
<summary><h2> Эндпоинты </h2></summary>

- >POST /hit - добавление статистики
- >GET /stats - получение статистики
</details>
