package ru.practicum.shareit.messages;

public enum LogMessages {
    TRY_ADD("Поступил запрос на добавление: {}"),
    ADD("Объект успешно добавлен: {}"),
    TRY_PATCH("Поступил запрос на частичное обновление: {}"),
    TRY_UPDATE("Поступил запрос на обновление: {}"),
    UPDATE("Объект успешно обновлен: {}"),
    TRY_GET_OBJECT("Поступил запрос на получение объекта по id: {}"),
    GET("Объект получен: {}"),
    TRY_REMOVE_OBJECT("Поступил запрос на удаление объекта: {}"),
    REMOVE("Объект удален по id: {}"),
    TRY_GET_ALL("Поступил запрос на предоставление списка {}"),
    GET_ALL_USERS("Предоставлен список: {}"),
    TRY_GET_SEARCH("Попытка поиска items by = {}.");


    private final String textLog;

    LogMessages(String textLog) {
        this.textLog = textLog;
    }

    @Override
    public String toString() {
        return textLog;
    }
}
