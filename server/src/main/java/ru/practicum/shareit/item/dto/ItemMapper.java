package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemForRequestDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;


public class ItemMapper {
    public static Item toItem(ItemDtoReq itemDtoReq, Long ownerId) {
        User owner = new User();
        owner.setId(ownerId);

        return Item.builder()
                .name(itemDtoReq.getName())
                .description(itemDtoReq.getDescription())
                .available(itemDtoReq.getAvailable())
                .owner(owner)
                .requestId(itemDtoReq.getRequestId())
                .build();
    }


    public static Item toItemFromDtoRes(ItemDtoRes itemDtoRes) {
        return Item.builder()
                .name(itemDtoRes.getName())
                .description(itemDtoRes.getDescription())
                .available(itemDtoRes.getAvailable())
                .requestId(itemDtoRes.getRequestId())
                .build();
    }

    public ItemDtoRes toItemDtoRes(Item item) {
        ItemDtoRes itemDtoRes = new ItemDtoRes();
        itemDtoRes.setId(item.getId());
        itemDtoRes.setName(item.getName());
        itemDtoRes.setDescription(item.getDescription());
        itemDtoRes.setAvailable(item.getAvailable());
        itemDtoRes.setOwnerId(item.getOwner().getId());
        itemDtoRes.setRequestId(item.getRequestId());
        return itemDtoRes;
    }


    public ItemDtoRes toItemImfoDtoRes(Item item, List<Comment> comments) {
        List<CommentDtoRes> commentDtos = comments.stream()
                .map(CommentMapper::toCommentDtoRes)
                .collect(Collectors.toList());
        ItemDtoRes itemDtoRes = new ItemDtoRes();
        itemDtoRes.setId(item.getId());
        itemDtoRes.setName(item.getName());
        itemDtoRes.setDescription(item.getDescription());
        itemDtoRes.setAvailable(item.getAvailable());
        itemDtoRes.setOwnerId(item.getOwner().getId());
        itemDtoRes.setRequestId(item.getRequestId());
        itemDtoRes.setComments(commentDtos);
        return itemDtoRes;
    }

    public static ItemForRequestDto toItemRequestForUserDto(Item item) {
        return ItemForRequestDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

}
