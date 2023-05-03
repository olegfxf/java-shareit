package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoReq;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RestController
@RequestMapping("/bookings")
public class BookingController {
    BookingService bookingService;
    ItemService itemService;
    UserService userService;
    UserRepository userRepository;
    ItemRepository itemRepository;
    BookingRepository bookingRepository;

    @Autowired
    public BookingController(BookingService bookingService,
                             UserRepository userRepository,
                             ItemRepository itemRepository,
                             BookingRepository bookingRepository,
                             ItemService itemService,
                             UserService userService) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.bookingRepository = bookingRepository;
        this.itemService = itemService;
        this.userService = userService;
    }

    static Integer count = 1;

    @PostMapping
    @ResponseBody
    public BookingDtoRes save(@RequestBody BookingDtoReq bookingDtoReq,
                              @RequestHeader("x-sharer-user-id") @NotEmpty String bookerId) {
        return BookingMapper.toBookingDtoRes(bookingService.save(Long.valueOf(bookerId), BookingMapper.toItem(bookingDtoReq)));
    }

    @PatchMapping("/{bookingId}")
    @ResponseBody
    public BookingDtoRes approveIt(@PathVariable Long bookingId,
                                   @RequestHeader("x-sharer-user-id") Long userId,
                                   @RequestParam boolean approved) {
        return BookingMapper.toBookingDtoRes(bookingService.approveIt(bookingId, userId, approved));
    }


    @GetMapping("/{bookingId}")
    @ResponseBody
    public BookingDtoRes getById(@PathVariable Long bookingId,
                                 @RequestHeader("x-sharer-user-id") Long ownerId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), ownerId);
        return BookingMapper.toBookingDtoRes(bookingService.getById(bookingId, ownerId));
    }


    @GetMapping
    @ResponseBody
    public List<BookingDtoRes> getAll(@RequestHeader("x-sharer-user-id") Long userId,
                                      @RequestParam(defaultValue = "ALL", required = false) String state) {
//        if (state == null)
//            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "бронирований");


        List<Booking> bookings = bookingService.getAll1(userId, state);
//        if (bookings == null)
//            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
//        System.out.println(bookings + " %%% " + bookings.size());
        List<BookingDtoRes> bookingDtoRes = bookings.stream()
                .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());

        bookingRepository.findAll().stream().forEach(System.out::println);
        log.debug(String.valueOf(LogMessages.GET), bookingDtoRes);
        return bookingDtoRes;


    }

    @GetMapping("/owner")
    @ResponseBody
    public List<BookingDtoRes> ownerGet(@RequestHeader("x-sharer-user-id") Long userId,
                                        @RequestParam(defaultValue = "ALL", required = false) String state) {

        List<Booking> owner = bookingService.ownerGet(userId, state);
        List<BookingDtoRes> ownerDtoRes = owner.stream()
                .map(e -> BookingMapper.toBookingDtoRes(e)).collect(Collectors.toList());


        return ownerDtoRes;
    }
}
