package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    @InjectMocks
    BookingService bookingService;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    UserService userService;
    @Mock
    UserRepository userRepository;


    Booking booking;
    User user = new User();
    Item item = new Item();
    long userId = 1L;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user.setId(userId);

        item.setId(1L);
        item.setAvailable(true);
        item.setOwner(user);

        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 1, 1, 0, 0))
                .end(LocalDateTime.of(2023, 1, 2, 0, 0))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
    }


    @Test
    void save() {
//        Long bookerId = 1L;
//        User booker = Mockito.mock(User.class);
//        booker = user;
//        Booking expectedBooking = booking;
//
//
//        when(itemService.getById(booking.getItem().getId()))
//                .thenReturn(item);
//        when(userService.getById(bookerId))
//                .thenReturn(user);
////        when(any(Long.class).equals(any(Long.class)))
////                .thenReturn(true);
////
////
//        when(bookingRepository.save(expectedBooking))
//                .thenReturn(expectedBooking);
//        Booking actualBooking = BookingMapper.toBookingFromDtoRes(bookingService.save(bookerId, expectedBooking));
//        assertEquals(expectedBooking, actualBooking);

    }

    @Test
    void dateValidation() {
    }

    @Test
    void approveIt() {
    }

    @Test
    void getById() {
        long bookingId = 1L;
        Booking expectedBooking = booking;
        when(bookingRepository.findBookingByIdAndBookerOrOwner(bookingId, userId))
                .thenReturn(expectedBooking);

        Booking actualBooking = BookingMapper.toBookingFromDtoRes(bookingService.getById(bookingId, userId));

        assertEquals(expectedBooking, actualBooking);
    }

    @Test
    void getAll() {
        int from = 4;
        int size = 2;
        String state = "ALL";
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(booking);
        User user = new User();
        Pageable pageable = PageRequest.of(from > 0 ? from / size : 0, size);
        when(userService.getById(1L))
                .thenReturn(user);
        when(bookingRepository.findAllByBookerOrderByIdDesc(user, pageable))
                .thenReturn(expectedBookings);

        List<Booking> actualBookings = bookingService.getAll(userId, state, from, size).stream()
                .map(e -> BookingMapper.toBookingFromDtoRes(e)).collect(Collectors.toList());

        assertEquals(expectedBookings, actualBookings);
    }


    @Test
    void ownerGet() {
        int from = 4;
        int size = 2;
        String state = "ALL";
        List<Booking> expectedBookings = new ArrayList<>();
        expectedBookings.add(booking);
        PageRequest pageRequest = PageRequest.of(from, size);
        when(userRepository.existsById(userId))
                .thenReturn(true);
        when(bookingRepository.findAll(userId, pageRequest))
                .thenReturn(expectedBookings);

        List<Booking> actualBookings = bookingService.ownerGet(userId, state, from, size).stream()
                .map(e -> BookingMapper.toBookingFromDtoRes(e)).collect(Collectors.toList());

        assertEquals(expectedBookings, actualBookings);
    }
}