package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;


public class ItemMapper {
    public static Item toItem(ItemDtoReq itemDtoReq, Long ownerId) {
        User owner = new User();
        owner.setId(itemDtoReq.getOwnerId());

        return Item.builder()
                .name(itemDtoReq.getName())
                .description(itemDtoReq.getDescription())
                .available(itemDtoReq.getAvailable())
                .owner(owner)
                .requestId(itemDtoReq.getRequest())
                .build();
    }

    public ItemDtoRes toItemDtoRes(Item item) {
        ItemDtoRes itemDtoRes = new ItemDtoRes();
        itemDtoRes.setId(item.getId());
        itemDtoRes.setName(item.getName());
        itemDtoRes.setDescription(item.getDescription());
        itemDtoRes.setAvailable(item.getAvailable());
        itemDtoRes.setOwnerId(item.getOwner().getId());
        itemDtoRes.setRequest(item.getRequestId());
        return itemDtoRes;
    }
}
