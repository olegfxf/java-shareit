package ru.practicum.shareit.request.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ItemForRequest {
    Long id;

    String name;

    String description;

    Boolean available;

    Long requestId;

 //   Long ownerId;
}
