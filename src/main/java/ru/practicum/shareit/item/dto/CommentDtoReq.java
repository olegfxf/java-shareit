package ru.practicum.shareit.item.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentDtoReq {
    @NotBlank
    private String text;
}
