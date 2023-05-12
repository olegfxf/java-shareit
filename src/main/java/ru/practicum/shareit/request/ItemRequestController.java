package ru.practicum.shareit.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        return itemRequestService.addItemRequest(itemRequest, userId);
    }

    @GetMapping("/all?from={from}&size={size}")
    public List<ItemRequestDto> getAllForUser(@RequestParam String from, @RequestParam String size,
                                              @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        return itemRequestService.getAllCustom(from, size, userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable Long itemId, @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        return itemRequestService.getById(itemId, userId);
    }

}
