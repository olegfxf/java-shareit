package ru.practicum.shareit.item;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @InjectMocks
    ItemService itemService;

    @Mock
    ItemRepository itemRepository;

//    @Mock
//    CommentRepository commentRepository;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    BookingRepository bookingRepository;
//
//    @Mock
//    Item mockItem;

    @Test
    void addItem() {
    }

    @Test
    void updateItem() {
//        List<Item> expectedItems = new ArrayList<>();
//        when(itemRepository.save(any()))
//                .thenReturn(expectedItems);
    }


    @Test
    void getAllByUserId() {
//        long itemId = 0L;
//        Item expectedItem = new Item();
//        when(itemRepository.findAll())
//                .thenReturn(Optional.of(expectedItem));
    }

    @Test
    void searchText() {
        List<Item> expectedItems = new ArrayList<>();
        String text = "text";
        when(itemRepository.search(text))
                .thenReturn(expectedItems);

        List<Item> actualItems = itemService.searchText(text).stream()
                .map(e -> ItemMapper.toItemFromDtoRes(e)).collect(Collectors.toList());

        assertEquals(expectedItems, actualItems);
    }

    @Test
    void getById() {
        long itemId = 0L;
        Item expectedItem = new Item();
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.getById(itemId);

        assertEquals(expectedItem, actualItem);
    }

    @IgnoreForbiddenApisErrors(reason = "all")
    @Test
    void addComment() {
//        long itemId = 0L;
//        long userId = 0L;
//        Comment expectedComment = new Comment();
//        Item expectedItem = new Item();
//        Item item = new Item();
//        mockItem = new Item();
//        User user = new User();
//        user.setId(1L);
//        user.setName("name");
//        user.setEmail("name@mail.ru");
//        item.setOwner(user);
//        mockItem.setOwner(user);
//        Booking booking = new Booking();
//        List<Booking> bookings = new ArrayList<>();
//        bookings.add(booking);
//        when(commentRepository.save(expectedComment))
//                .thenReturn(expectedComment);
//
//        when(itemRepository.findById(itemId))
//                .thenReturn(Optional.of(expectedItem));
//
//        when(userRepository.findById(userId))
//                .thenReturn(Optional.of(user));
//
//        when(bookingRepository.existsBookingByBookerAndItemAndStatus(user, item, APPROVED))
//                .thenReturn(true);
//
//        doReturn(bookings).
//                when(bookingRepository).findByBookerAndItemAndEndBefore(user, item, LocalDateTime.now());
//
////        when(mockItem.getOwner().getId().equals(userId))
////                .thenReturn(false);
//
//        Comment actualComment = itemService.addComment(expectedComment, itemId, userId);
//
//        assertEquals(expectedComment, actualComment);
    }

    @Test
    void removeById1() {
        long itemId = 0L;
        itemRepository.deleteById(itemId);

        verify(itemRepository, times(1)).deleteById(eq(itemId));
    }
}