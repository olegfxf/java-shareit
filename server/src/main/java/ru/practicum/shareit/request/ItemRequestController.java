package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequest itemRequest,
                                         @RequestHeader("x-sharer-user-id") Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "добавление запроса на вещь");
        return itemRequestService.addItemRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestDtoForUser> getAllForOwner(@RequestHeader("x-sharer-user-id") Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "просмотр запросов пользователя с id " + userId);
        return itemRequestService.getAllForOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoForUser> getAllForUser(@RequestParam Integer from,
                                                     @RequestParam Integer size,
                                                     @RequestHeader("x-sharer-user-id") Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "просмотр запросов вещей всех пользователей");
        return itemRequestService.getAllForUser(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoForUser getById(@PathVariable Long requestId,
                                         @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), "поступил запрос на данные по запросу  с id " + requestId);
        return itemRequestService.getById(requestId, userId);
    }

}
