package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoReq;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    BookingService bookingService;

    @PostMapping
    @ResponseBody
    public BookingDtoRes save(@RequestBody BookingDtoReq bookingDtoReq,
                              @RequestHeader("x-sharer-user-id") Long bookerId) {
        return bookingService.save(bookerId, BookingMapper.toBooking(bookingDtoReq));
    }

    @PatchMapping("/{bookingId}")
    @ResponseBody
    public BookingDtoRes approveRequest(@PathVariable Long bookingId,
                                        @RequestHeader("x-sharer-user-id") Long userId,
                                        @RequestParam boolean approved) {
        return bookingService.approveIt(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseBody
    public BookingDtoRes getById(@PathVariable Long bookingId,
                                 @RequestHeader("x-sharer-user-id") Long ownerId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), ownerId);
        return bookingService.getById(bookingId, ownerId);
    }

    @GetMapping
    @ResponseBody
    public List<BookingDtoRes> getAll(@Valid @RequestHeader("x-sharer-user-id") Long userId,
                                      @RequestParam Integer from,
                                      @RequestParam Integer size,
                                      @RequestParam String state) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "бронирований");

        if (size < 0 || from < 0)
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        return bookingService.getAll(userId, state, from, size);
    }

    @GetMapping("/owner")
    @ResponseBody
    @Validated
    public List<BookingDtoRes> ownerGet(@Valid @RequestHeader("x-sharer-user-id") Long userId,
                                        @RequestParam Integer from,
                                        @RequestParam Integer size,
                                        @RequestParam String state) {
        return bookingService.ownerGet(userId, state, from, size);
    }
}
