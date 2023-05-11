package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoReq;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.constraints.NotEmpty;
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
                              @RequestHeader("x-sharer-user-id") @NotEmpty Long bookerId) {
        return bookingService.save(bookerId, BookingMapper.toBooking(bookingDtoReq));
    }

    @PatchMapping("/{bookingId}")
    @ResponseBody
    public BookingDtoRes approveIt(@PathVariable Long bookingId,
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
    public List<BookingDtoRes> getAll(@RequestHeader("x-sharer-user-id") Long userId,
                                      @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "бронирований");
        return bookingService.getAll(userId, state);
    }

    @GetMapping("/owner")
    @ResponseBody
    public List<BookingDtoRes> ownerGet(@RequestHeader("x-sharer-user-id") Long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.ownerGet(userId, state);
    }
}
