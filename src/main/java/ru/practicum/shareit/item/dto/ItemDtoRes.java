package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.LastNextRecordBooking;

import java.util.List;
import java.util.Optional;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemDtoRes {
    long id;

    String name;

    String description;

    Boolean available;

    Long ownerId;

    Long request;

    Optional<LastNextRecordBooking> lastBooking;

    Optional<LastNextRecordBooking> nextBooking;

    List<CommentDtoRes> comments;

}
