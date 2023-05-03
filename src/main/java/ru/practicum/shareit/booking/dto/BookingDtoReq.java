package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDtoReq {
    @Positive
    long id;

    LocalDateTime start;

    LocalDateTime end;

    @Positive
    Long itemId;

    @Positive
    Long bookerId;

    Status status;

}
