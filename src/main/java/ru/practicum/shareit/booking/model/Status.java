package ru.practicum.shareit.booking.model;

public enum Status {
    WAITING, // заявка на бронирование находится в статусе ожидания
    APPROVED, // заявка на бронирование утверждена
    REJECTED, // заявка на бронирование отклонена
    CANCELED  // заявка на бронирование завершена
}