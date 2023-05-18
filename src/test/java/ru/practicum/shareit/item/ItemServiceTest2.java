package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
//@TestPropertySource(properties = { "db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemServiceTest2 {

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final ObjectMapper mapper;


    long itemId;

    long userId;
    long user2Id;
    User user = new User();
    User user2 = new User();

    Booking booking;


    ItemRequest itemRequest;

    ItemDtoReq itemDtoReq;

    @BeforeEach
    public void setUp() {
        itemId = 1L;

        userId = 1L;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");
        //  User user1 = new User(1,"name1", "name1@mail.ru");
        user2Id = 2;
        user2.setId(user2Id);
        user2.setName("name2");
        user2.setEmail("name2@mail.ru");

        long itemRequestId = 1L;
        itemRequest = ItemRequest.builder()
                .id(itemRequestId)
                .description("description")
                .requester(user)
                .created(LocalDateTime.of(2023, 2, 1, 0, 0))
                .build();

        itemDtoReq = ItemDtoReq.builder()
                .name("name")
                .description("description")
                .available(true)
                .ownerId(1L)
                .requestId(1L)
                .build();


    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addItem() {
        userService.save(user);
        Item item = new Item();
        item.setName("name1");
        item.setDescription("description2");
        itemRepository.save(item);
        ItemDtoReq expectedItem = itemDtoReq;
        ItemDtoRes actualItem = itemService.addItem(itemDtoReq, userId);

        assertEquals(expectedItem.getName(), actualItem.getName());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void updateItem() {
        userService.save(user);
        Item item = new Item();
        item.setName("name1");
        item.setDescription("description1");
        item.setOwner(user);
        itemRepository.save(item);
        itemDtoReq.setDescription("descriptionNew");

        ItemDtoRes actualItemDtoRes = itemService.updateItem(itemId, itemDtoReq, userId);

        assertEquals("descriptionNew", actualItemDtoRes.getDescription());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getAllByUserId() {
        userService.save(user);

        Item item = new Item();
        item.setName("nameItem");
        item.setOwner(user);
        item.setDescription("descriptionItem");
        itemRepository.save(item);

        List<ItemDtoRes> items = itemService.getAllByUserId(userId);
        assertEquals(1, items.size());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addComment() {
//        userService.save(user);
//        System.out.println(userService.getAll());
//
//        System.out.println(user2);
//        userService.save(user2);
//        System.out.println(userService.getAll());
//
//        Item item = new Item();
//        item.setName("nameItem");
//        item.setOwner(user2);
//        item.setDescription("descriptionItem");
//        itemRepository.save(item);
//
//        Comment expectedComment = new Comment();
//        expectedComment.setId(1L);
//        expectedComment.setItem(item);
//        user.setId(userId);
//        expectedComment.setAuthor(user);
//        expectedComment.setCreated(LocalDateTime.now());
//        expectedComment.setText("text");
//
//        booking = Booking.builder()
//                .start(LocalDateTime.of(2023, 6, 1, 0, 0))
//                .end((LocalDateTime.of(2024, 2, 1, 1, 1)))
//                .status(Status.APPROVED)
//                .booker(user)
//                .item(item)
//                .id(1L)
//                .build();
//
//        bookingRepository.save(booking);
//
//        Comment comment = itemService.addComment(expectedComment, itemId, userId);

    }


}
