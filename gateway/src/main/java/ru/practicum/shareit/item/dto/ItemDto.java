package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ItemDto {

    private Long id;

//   @NotEmpty
    private String name;

//    @NotEmpty
    private String description;

//    @NotNull
    private Boolean available;

    private Long requestId;

    private List<CommentDto> comments;

}
