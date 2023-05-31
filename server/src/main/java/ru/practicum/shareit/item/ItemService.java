package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.LastNextRecordBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.Status.APPROVED;

//@Transactional(readOnly = true)
@Service
@Slf4j
public class ItemService extends AbstractServiceImpl<Item, ItemRepository> {
    ItemRepository itemRepository;
    BookingRepository bookingRepository;
    UserService userService;
    LastNextRecordBooking lastNextRecordBooking;
    CommentRepository commentRepository;
    UserRepository userRepository;
    ItemMapper itemMapper = new ItemMapper();

    @Autowired
    public ItemService(ItemRepository itemRepository,
                       LastNextRecordBooking lastNextRecordBooking,
                       UserService userService,
                       CommentRepository commentRepository,
                       BookingRepository bookingRepository,
                       UserRepository userRepository) {
        super(itemRepository);
        this.itemRepository = itemRepository;
        this.lastNextRecordBooking = lastNextRecordBooking;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public ItemDtoRes addItem(ItemDtoReq itemDtoReq, Long ownerId) {

        if (!userService.getAll().stream().anyMatch(e -> ownerId.equals(e.getId()))) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }

        if (itemRepository.findAll().stream().anyMatch(e -> itemDtoReq.getName().equals(e.getName())))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        Item item = itemMapper.toItem(itemDtoReq, ownerId);
        User owner = userService.getById(ownerId);
        item.setOwner(owner);

        return itemMapper.toItemDtoRes(save(item));
    }

    @Transactional
    public ItemDtoRes updateItem(Long itemId, ItemDtoReq itemDtoReq, Long ownerId) {
        Item item;
        try {
            item = this.getById(itemId);
        } catch (NotFoundException e) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }

        if (!ownerId.equals(item.getOwner().getId())) {
            log.debug(String.valueOf(HandlerMessages.SERVER_ERROR));
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }

        Item item1 = ItemMapper.toItem(itemDtoReq, ownerId);

        if (item1.getName() != null) item.setName(item1.getName());
        if (item1.getDescription() != null) item.setDescription(item1.getDescription());
        if (item1.getAvailable() != null) item.setAvailable(item1.getAvailable());

        log.debug(String.valueOf(LogMessages.TRY_PATCH), item);
        return itemMapper.toItemDtoRes(itemRepository.save(item));
    }

    public List<ItemDtoRes> getAllByUserId(Long userId) {
        List<ItemDtoRes> itemDtoRes1 = itemRepository.findAll().stream().filter(e -> e.getOwner().getId().equals(userId))
                .map(item -> itemMapper.toItemDtoRes(item)).collect(toList());

        List<ItemDtoRes> itemDtoRes = itemDtoRes1.stream().map(e -> getById(e.getId(), userId)).collect(toList());
        ItemDtoComparator itemDtoComparator = new ItemDtoComparator();
        itemDtoRes.sort(itemDtoComparator);
        return itemDtoRes;
    }

    public class ItemDtoComparator implements Comparator<ItemDtoRes> {
        @Override
        public int compare(ItemDtoRes item1, ItemDtoRes item2) {
            return ((Long) (item1.getId() - item2.getId())).intValue();
        }
    }


    public List<ItemDtoRes> searchText(String text) {
        List<Item> items = itemRepository.search(text);

        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH));
        return items.stream().map(e -> itemMapper.toItemDtoRes(e)).collect(toList());
    }

    public ItemDtoRes getById(Long itemId, Long userId) {
        if (!itemRepository.findById(itemId).isPresent()) {
            throw new NotFoundException(ExceptionMessages.NOT_OBJECT);
        }

        Item item = itemRepository.findById(itemId).get();
        ItemDtoRes itemDtoRes = itemMapper.toItemImfoDtoRes(item, commentRepository.findAllByItem(item));
        Long ownerId = item.getOwner().getId();
        if (Long.valueOf(userId).equals(ownerId)) {
            itemDtoRes.setNextBooking(lastNextRecordBooking.get(itemId, true, Long.valueOf(userId)));
            itemDtoRes.setLastBooking(lastNextRecordBooking.get(itemId, false, Long.valueOf(userId)));
        }

        log.debug(String.valueOf(LogMessages.GET), itemDtoRes);
        return itemDtoRes;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CommentDtoRes addComment(Comment comment1, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).get();
        User user = userRepository.findById(userId).get();

        if (!bookingRepository.existsBookingByBookerAndItemAndStatus(user, item, APPROVED)) {
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));
        }

        if (bookingRepository.findByBookerAndItemAndEndBefore(user, item, LocalDateTime.now()).isEmpty())
            throw new ValidationException(String.valueOf((HandlerMessages.VALID)));

        if (item.getOwner().getId().equals(userId))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        Comment comment = comment1;
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDtoRes(commentRepository.save(comment));
    }


    public ItemDtoRes removeById1(Long itemId) {
        Item item = itemRepository.findById(itemId).get();
        itemRepository.deleteById(itemId);
        return itemMapper.toItemDtoRes(item);

    }


}
