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
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDtoRes;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(classes = {
        ShareItApp.class,
        H2TestProfileJPAConfig.class})
@ActiveProfiles("test")
@Transactional
//@TestPropertySource(properties = { "db.name=test"})
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class ItemServiceTest {

    private final ItemService itemService;

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final BookingService bookingService;

    private final ObjectMapper mapper;


    long itemId;
    long userId;
    long user2Id;
    User user = new User();
    User user2 = new User();
    Item item = new Item();
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

        user2Id = 2L;
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

        userService.save(user);

        item = new Item();
        item.setName("nameItem");
        item.setOwner(user);
        item.setDescription("descriptionItem");
        itemRepository.save(item);


    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addItem() {
        ItemDtoReq expectedItem = itemDtoReq;
        ItemDtoRes actualItem = itemService.addItem(itemDtoReq, userId);

        assertEquals(expectedItem.getName(), actualItem.getName());

        assertThrows(NotFoundException.class, () -> itemService
                .addItem(itemDtoReq, 5L));

        item.setName("name5");
        itemRepository.save(item);
        assertThrows(NotFoundException.class, () -> itemService
                .addItem(itemDtoReq, 5L));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void updateItem() {
        itemDtoReq.setDescription("descriptionNew");

        ItemDtoRes actualItemDtoRes = itemService.updateItem(itemId, itemDtoReq, userId);
        assertEquals("descriptionNew", actualItemDtoRes.getDescription());

        assertThrows(NotFoundException.class, () -> itemService
                .updateItem(5L, itemDtoReq, userId));

        assertThrows(NotFoundException.class, () -> itemService
                .updateItem(itemId, itemDtoReq, 5L));
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getAllByUserId() {
        List<ItemDtoRes> items = itemService.getAllByUserId(userId);
        assertEquals(1, items.size());
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void searchText() {
        List<ItemDtoRes> items = itemService.searchText("nameI");
        assertEquals("nameItem", items.get(0).getName());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void getById() {
        ItemDtoRes itemDtoRes = itemService.getById(item.getId(), user.getId());
        assertEquals("nameItem", itemDtoRes.getName());

        assertThrows(NotFoundException.class, () -> itemService
                .getById(5L, user.getId()));
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addComment() {
        Comment expectedComment = new Comment();
        expectedComment.setId(1L);
        expectedComment.setItem(item);
        expectedComment.setAuthor(user);
        expectedComment.setCreated(LocalDateTime.now());
        expectedComment.setText("text");

        userId = 1L;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userService.getAll1();
        userService.save(user);
        userService.save(user2);
//

//        item = new Item();
//        item.setName("nameItem");
//        item.setOwner(user);
//        item.setDescription("descriptionItem");
//        itemRepository.deleteAll();
//        itemRepository.save(item);

        //  userService.save(user);
//        userService.save(user2);
//
        item = new Item();
        item.setName("nameItem");
        item.setDescription("descriptionItem");
        item.setAvailable(true);
        item.setOwner(user2);
        itemRepository.save(item);

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 4, 1, 0, 0))
                .end((LocalDateTime.of(2023, 5, 1, 1, 1)))
                .status(Status.APPROVED)
                .booker(user)
                .item(item)
                .build();

        bookingRepository.save(booking);

        System.out.println(item.getId());
        System.out.println(user.getId());
        System.out.println("JJJJ");
        bookingRepository.findAll().stream().forEach(System.out::println);

        CommentDtoRes commentDtoRes = itemService.addComment(expectedComment, item.getId(), user.getId());
        assertEquals("text", commentDtoRes.getText());

    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addCommentPlus() {
        Comment expectedComment = new Comment();
        expectedComment.setId(1L);
        expectedComment.setItem(item);
        expectedComment.setAuthor(user);
        expectedComment.setCreated(LocalDateTime.now());
        expectedComment.setText("text");

        userId = 1L;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userService.getAll1();
        userService.save(user);
        userService.save(user2);
//

//        item = new Item();
//        item.setName("nameItem");
//        item.setOwner(user);
//        item.setDescription("descriptionItem");
//        itemRepository.deleteAll();
//        itemRepository.save(item);

        //  userService.save(user);
//        userService.save(user2);
//
        item = new Item();
        item.setName("nameItem");
        item.setDescription("descriptionItem");
        item.setAvailable(true);
        item.setOwner(user2);
        itemRepository.save(item);

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 4, 1, 0, 0))
                .end((LocalDateTime.of(2023, 6, 1, 1, 1)))
                .status(Status.APPROVED)
                .booker(user)
                .item(item)
                .build();

        bookingRepository.save(booking);

        System.out.println(item.getId());
        System.out.println(user.getId());
        System.out.println("JJJJ");
        bookingRepository.findAll().stream().forEach(System.out::println);


        assertThrows(ValidationException.class, () -> itemService
                .addComment(expectedComment, item.getId(), user.getId()));


    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
    @Test
    void addCommentPlus2() {
        Comment expectedComment = new Comment();
        expectedComment.setId(1L);
        expectedComment.setItem(item);
        expectedComment.setAuthor(user);
        expectedComment.setCreated(LocalDateTime.now());
        expectedComment.setText("text");

        userId = 1L;
        user.setId(userId);
        user.setName("name");
        user.setEmail("name@mail.ru");
        userService.getAll1();

        userService.save(user);
        //  userService.save(user2);

        item = new Item();
        item.setName("nameItem");
        item.setDescription("descriptionItem");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);

        booking = Booking.builder()
                .start(LocalDateTime.of(2023, 4, 1, 0, 0))
                .end((LocalDateTime.of(2023, 5, 1, 1, 1)))
                .status(Status.APPROVED)
                .booker(user)
                .item(item)
                .build();

        bookingRepository.save(booking);

        System.out.println(item.getId());
        System.out.println(user.getId());
        System.out.println("JJJJ");
        bookingRepository.findAll().stream().forEach(System.out::println);


        assertThrows(ValidationException.class, () -> itemService
                .addComment(expectedComment, item.getId(), user.getId()));
    }


}
