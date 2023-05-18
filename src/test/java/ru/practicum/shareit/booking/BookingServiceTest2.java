package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest2 {

    private final ItemRepository itemRepository;


    private final UserService userService;

    private final BookingService bookingService;


    long itemId;

    long userId;
    long user2Id;
    User user = new User();
    User user2 = new User();

    Booking booking;

    @BeforeEach
    public void setUp() {
        itemId = 1L;

        userId = 1l;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");

        user2Id = 2;
        user2.setId(user2Id);
        user2.setName("name2");
        user2.setEmail("name2@mail.ru");
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void save() {
        userService.save(user);
        userService.save(user2);

        Item item = new Item();
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

        BookingDtoRes actual = bookingService.save(user2Id, booking);

        assertEquals(item, actual.getItem());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void approveIt() {
        userService.save(user);
        userService.save(user2);

        Item item = new Item();
        item.setName("nameItem");
        item.setDescription("descriptionItem");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 6, 1, 0, 0))
                .end((LocalDateTime.of(2024, 2, 1, 1, 1)))
                .status(Status.WAITING)
                .booker(user)
                .item(item)
                .id(1L)
                .build();
        bookingService.save(user2Id, booking);

        BookingDtoRes actual = bookingService.approveIt(1L, userId, true);

        assertEquals(Status.APPROVED, actual.getStatus());
    }


}
