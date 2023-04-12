package ru.practicum.shareit.messages;

public enum LogMessages {
    TRY_ADD("Попытка добавить: {}"),
    ADD("Объект успешно добавлен: {}"),
    TRY_UPDATE("Попытка обновить: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    TRY_GET_OBJECT("Попытка получить объект по id: {}"),
    GET("Объект получен: {}"),
    TRY_REMOVE("Попытка удалить: {}"),
    REMOVE("Объект удален по id: {}"),
    TRY_GET_ALL("Поступил запрос на предоставление списка {}"),
    GET_ALL_USERS("Предоставлен список: {}"),



    TRY_GET_POPULAR("Попытка получить популярный объект: {}"),

    TRY_REMOVE_LIKE("Попытка удалить поставленный лайк: film_id {}, user_id {}"),
    TRY_ADD_LIKE("Попытка поставить лайк: film_id {}, user_id {}"),
    TRY_ADD_FRIEND("Попытка добавить друга: {}"),
    TRY_REMOVE_FRIEND("Попытка удалить друга: {}"),
    TRY_GET_FRIENDS("Попытка получить список друзей: {}"),
    TRY_GET_CORPORATE_FRIENDS("Попытка получить общих друзей: {}"),
    TRY_REMOVE_OBJECT("Попытка удалить объект: {}"),
    TRY_GET_RECOMMENDATIONS("Попытка получить рекомендации по фильмам для пользователя с id: {}"),
    MISSING("Такого объекта не существует"),
    COUNT("Количество объектов: {}"),
    FRIEND_DONE("Пользователи с id: {} и {} стали друзьями"),
    FRIEND_CANCEL("Пользователи с id: {} и {} больше не друзья"),
    LIKE_DONE("Пользователь с id: {} поставил лайк фильму с id: {}"),
    LIKE_CANCEL("Пользователь с id: {} удалил лайк у фильма с id: {}"),
    LIST_OF_FRIENDS("Список общих друзей пользователей: {}"),
    TRY_GET_DIRECTOR_FILM("Попытка получение списка фильмов режиссера directorId={}, sort={}."),
    TRY_GET_SEARCH("Попытка поиска: query = {}, by = {}."),
    LIST_OF_RECOMMENDATIONS("Предоставлен список рекомендованых фильмов пользователю с id: {}"),
    TRY_DELETE("Попытка удалить: {}"),
    REVIEW_LIKE_DONE("Пользователь с id: {} поставил лайк отзыву с id: {}"),
    REVIEW_LIKE_CANCEL("Пользователь с id: {} удалил лайк у отзыва с id: {}"),
    REVIEW_DISLIKE_DONE("Пользователь с id: {} поставил дизлайк отзыву с id: {}"),
    REVIEW_DISLIKE_CANCEL("Пользователь с id: {} удалил дизлайк у отзыва с id: {}"),
    TRY_GET_COMMON_FILMS("Попытка получить список общих просмотренных фильмов для пользователей с id: {} и id: {}"),
    LIST_OF_COMMON_FILMS("Предоставлен список общих просмотренных фильмов для пользователей с id: {} и id: {}"),
    TRY_GET_EVENT_FEED("Попытка получения ленты событий с пользователем id: {}");

    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}
