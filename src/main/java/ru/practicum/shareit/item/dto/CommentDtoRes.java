package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CommentDtoRes {
    private Long id;

    @NotBlank
    private String text;

    private String authorName;

    private LocalDateTime created;
}
