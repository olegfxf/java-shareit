package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;

    String description;

    User requester;

    LocalDateTime created;
}

