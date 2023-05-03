package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.SmallBooking;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDtoRes {
    long id;

    String name;

    String description;

    Boolean available;

    Long ownerId;

    Long request;

    Optional<SmallBooking> lastBooking;

    Optional<SmallBooking> nextBooking;

    List<CommentDtoRes> comments;

}
