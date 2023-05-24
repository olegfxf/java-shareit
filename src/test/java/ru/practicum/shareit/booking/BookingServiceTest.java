package ru.practicum.shareit.booking;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.H2TestProfileJPAConfig;
import ru.practicum.shareit.ShareItApp;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.UnexpectedErrorException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    long itemId;
    long userId;
    long user2Id;
    User user = new User();
    User user2 = new User();
    Item item = new Item();
    Booking booking;

    @BeforeEach
    public void setUp() {
        itemId = 1L;

        userId = 1L;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");

        user2Id = 2L;
        user2.setId(user2Id);
        user2.setName("name2");
        user2.setEmail("name2@mail.ru");

        userService.save(user);
        userService.save(user2);

        item = new Item();
        item.setName("nameItem");
        item.setDescription("descriptionItem");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 6, 1, 0, 0))
                .end((LocalDateTime.of(2024, 2, 1, 1, 1)))
                .status(Status.APPROVED)
                .booker(user)
                .item(item)
                .id(1L)
                .build();
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void save() {
        BookingDtoRes actual = bookingService.save(user2Id, booking);
        assertEquals(item, actual.getItem());
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionAvailable() {
        item.setAvailable(false);
        itemRepository.save(item);
        assertThrows(ValidationException.class, () -> bookingService
                .save(user2Id, booking));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionBookerIdEqualsOwnerId() {
        assertThrows(NotFoundException.class, () -> bookingService
                .save(userId, booking));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionStartIsNull() {
        booking.setStart(null);
        assertThrows(ValidationException.class, () -> bookingService
                .save(user2Id, booking));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionStartAfterEnd() {
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingService
                .save(user2Id, booking));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionStartEqualEnd() {
        LocalDateTime myNow = LocalDateTime.now().plusDays(1);
        booking.setStart(myNow);
        booking.setEnd(myNow);
        assertThrows(ValidationException.class, () -> bookingService
                .save(user2Id, booking));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void saveExceptionStartBeforeNow() {
        booking.setStart(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingService
                .save(user2Id, booking));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void approveIt() {
        bookingService.save(user2Id, booking);

        BookingDtoRes actual = bookingService.approveIt(1L, userId, true);
        assertEquals(Status.APPROVED, actual.getStatus());

        assertThrows(NotFoundException.class, () -> bookingService
                .approveIt(3L, userId, true));

        assertThrows(NotFoundException.class, () -> bookingService
                .approveIt(1L, 3L, true));

        booking.setStatus(Status.APPROVED);
        assertThrows(ValidationException.class, () -> bookingService
                .approveIt(1L, userId, true));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getById() {
        bookingService.save(user2Id, booking);

        BookingDtoRes bookingDtoRes = bookingService.getById(1L, 2L);
        assertEquals(Status.WAITING, bookingDtoRes.getStatus());

        assertThrows(NotFoundException.class, () -> bookingService
                .getById(3L, 2L));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getAll() {
        List<BookingDtoRes> expectedBookings = new ArrayList<>();
        expectedBookings.add(BookingMapper.toBookingDtoRes(booking));

        assertThrows(UnexpectedErrorException.class, () -> bookingService
                .getAll(user2Id, null, -1, 2));

        bookingService.save(user2Id, booking); // all
        List<BookingDtoRes> actual = bookingService.getAll(user2Id, State.ALL.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().minusDays(1)); // current
        booking.setEnd(LocalDateTime.now().plusDays(1));
        bookingRepository.save(booking);
        actual = bookingService.getAll(user2Id, State.CURRENT.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().minusDays(2)); // past
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);
        actual = bookingService.getAll(user2Id, State.PAST.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().plusDays(1)); // future
        booking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking);
        actual = bookingService.getAll(user2Id, State.FUTURE.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStatus(Status.WAITING); // waiting
        bookingRepository.save(booking);
        actual = bookingService.getAll(user2Id, State.WAITING.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStatus(Status.REJECTED); // rejected
        bookingRepository.save(booking);
        actual = bookingService.getAll(user2Id, State.REJECTED.toString(), -1, 2);
        assertEquals(user2, actual.get(0).getBooker());
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void ownerGet() {
        List<BookingDtoRes> expectedBookings = new ArrayList<>();
        expectedBookings.add(BookingMapper.toBookingDtoRes(booking));

        assertThrows(UnexpectedErrorException.class, () -> bookingService
                .ownerGet(user2Id, null, 0, 2));

        assertThrows(InternalException.class, () -> bookingService
                .ownerGet(5L, State.ALL.toString(), 0, 2));

        bookingService.save(user2Id, booking); // all
        List<BookingDtoRes> actual = bookingService.ownerGet(userId, State.ALL.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().minusDays(1)); // current
        booking.setEnd(LocalDateTime.now().plusDays(1));
        bookingRepository.save(booking);
        actual = bookingService.ownerGet(userId, State.CURRENT.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().minusDays(2)); // past
        booking.setEnd(LocalDateTime.now().minusDays(1));
        bookingRepository.save(booking);
        actual = bookingService.ownerGet(userId, State.PAST.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStart(LocalDateTime.now().plusDays(1)); // future
        booking.setEnd(LocalDateTime.now().plusDays(2));
        bookingRepository.save(booking);
        actual = bookingService.ownerGet(userId, State.FUTURE.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStatus(Status.WAITING); // waiting
        bookingRepository.save(booking);
        actual = bookingService.ownerGet(userId, State.WAITING.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

        booking.setStatus(Status.REJECTED); // rejected
        bookingRepository.save(booking);
        actual = bookingService.ownerGet(userId, State.REJECTED.toString(), 0, 2);
        assertEquals(user2, actual.get(0).getBooker());

    }


}
