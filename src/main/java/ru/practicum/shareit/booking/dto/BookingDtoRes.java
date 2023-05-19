package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookingDtoRes {
    @Positive
    long id;

    @JsonFormat
    @FutureOrPresent
    LocalDateTime start;

    @JsonFormat
    @Future
    LocalDateTime end;

    Item item;

    User booker;

    Status status;

}
