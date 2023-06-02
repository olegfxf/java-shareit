package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDtoReq {
    long id;

    LocalDateTime start;

    LocalDateTime end;

    Long itemId;

    Long bookerId;
}
