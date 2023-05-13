package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requester(itemRequest.getRequester())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDtoForUser toItemRequestDtoForUser(ItemRequest itemRequest, List<Item> items) {
        ItemRequestDtoForUser itemRequestDtoForUser = new ItemRequestDtoForUser();
        itemRequestDtoForUser.setId(itemRequest.getId());
        itemRequestDtoForUser.setDescription(itemRequest.getDescription());
        itemRequestDtoForUser.setCreated(itemRequest.getCreated());

        List<ItemForRequest> itemForRequests = items.stream()
                .map(ItemMapper::toItemRequestForUserDto)
                .collect(Collectors.toList());
        itemRequestDtoForUser.setItems(itemForRequests);

        return itemRequestDtoForUser;
    }
}
