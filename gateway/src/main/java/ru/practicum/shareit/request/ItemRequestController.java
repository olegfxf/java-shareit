package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @NotNull @Min(value = 1) @RequestHeader("X-Sharer-User-Id") Long requestorId,
            @Validated @RequestBody RequestDto requestDto) {
        return requestClient.createItemRequest(requestDto, requestorId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllForOwner(
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getItemRequestsByRequestorId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllForUser(@RequestParam(value = "from", defaultValue = "0") @Positive @Min(0) Integer from,
                                                @RequestParam(value = "size", defaultValue = "20") @Positive @Min(2) Integer size,
                                                @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        return requestClient.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(
            @NotNull @PathVariable Long requestId,
            @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return requestClient.getItemRequestById(requestId, userId);
    }
}
