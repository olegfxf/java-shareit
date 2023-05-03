package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.SmallBooking;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.Status.APPROVED;

@Service
@Slf4j
public class ItemService extends AbstractServiceImpl<Item, ItemRepository> {
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    SmallBooking smallBooking;
    CommentRepository commentRepository;
    UserRepository userRepository;
    ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       SmallBooking smallBooking,
                       CommentRepository commentRepository,
                       BookingRepository bookingRepository,
                       ItemMapper itemMapper,
                       UserRepository userRepository) {
        super(itemRepository);
        this.itemRepository = itemRepository;
        this.smallBooking = smallBooking;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemMapper = itemMapper;
        this.userRepository = userRepository;
    }


    public List<ItemDtoRes> getAllByOwnerId1(Long userId) {
        List<ItemDtoRes> itemDtoRes1 = itemRepository.findAll().stream().filter(e -> e.getOwner().getId().equals(userId))
                .map(item -> itemMapper.toItemDtoRes(item)).collect(toList());

        List<ItemDtoRes> itemDtoRes = itemDtoRes1.stream().map(e -> getById(e.getId(), userId)).collect(toList());
        return itemDtoRes;
    }

    public List<Item> search(String text) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH));
        return itemRepository.search(text);
    }

    public ItemDtoRes getById(Long itemId, Long userId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        }

        Item item = itemRepository.findById(itemId).get();
        ItemDtoRes itemDtoRes = itemMapper.toItemDtoRes(item);
        Long ownerId = item.getOwner().getId();
        if (Long.valueOf(userId).equals(ownerId)) {
            itemDtoRes.setNextBooking(smallBooking.get(itemId, true, Long.valueOf(userId)));
            itemDtoRes.setLastBooking(smallBooking.get(itemId, false, Long.valueOf(userId)));
        }

        log.debug(String.valueOf(LogMessages.GET), itemDtoRes);
        return itemDtoRes;
    }


    public Comment addComment(Comment comment1, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).get();
        User user = userRepository.findById(userId).get();

        if (!bookingRepository.existsBookingByBookerAndItemAndStatus(user, item, APPROVED))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (bookingRepository.findByBookerAndItemAndEndBefore(user, item, LocalDateTime.now()).isEmpty())
            throw new ValidationException(String.valueOf((HandlerMessages.VALID)));

//        System.out.println(item);
//        System.out.println(user);
//        System.out.println(userId + "  uuu " + item.getOwner().getId());
        if (item.getOwner().getId() == userId) {
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));
        }

        Comment comment = comment1;
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        commentRepository.save(comment);

        return commentRepository.save(comment);
    }


}
