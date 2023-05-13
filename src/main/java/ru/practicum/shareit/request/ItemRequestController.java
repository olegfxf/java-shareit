package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForUser;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    public ItemRequestDto addItemRequest(@Valid @RequestBody ItemRequest itemRequest,
                                         @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "добавление запроса на вещь");
        return itemRequestService.addItemRequest(userId, itemRequest);
    }

    @GetMapping
    public List<ItemRequestDtoForUser> getAllForOwner(@RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "просмотр запросов пользователя с id " + userId);
        return itemRequestService.getAllForOwner(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoForUser> getAllForUser(@RequestParam(value = "from", defaultValue = "0") @Positive  @Min(0) Integer from,
                                              @RequestParam(value = "size", defaultValue = "20") @Positive @Min(2) Integer size,
                                              @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "просмотр запросов вещей всех пользователей");
        return itemRequestService.getAllForUser(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoForUser getById(@PathVariable Long requestId, @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), "поступил запрос на данные по запросу  с id " + requestId);
        return itemRequestService.getById(requestId);
    }

}
