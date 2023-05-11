package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.abstracts.AbstractServiceImpl;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.LastNextRecordBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.practicum.shareit.booking.model.Status.APPROVED;

@Transactional(readOnly = true)
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

    public ItemDtoRes addItem(ItemDtoReq itemDtoReq, String ownerId) {

        if (!userService.getAll().stream().anyMatch(e -> Long.valueOf(ownerId).equals(e.getId()))) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }
        if (itemRepository.findAll().stream().anyMatch(e -> itemDtoReq.getName().equals(e.getName())))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));

        Item item = itemMapper.toItem(itemDtoReq, Long.valueOf(ownerId));
        User owner = userService.getById(Long.valueOf(ownerId));
        item.setOwner(owner);

        return itemMapper.toItemDtoRes(save(item));
    }

    @Transactional
    public ItemDtoRes updateItem(Long id, JsonMergePatch patch, String ownerId) {
        Item item = this.getById(id);

        if (!Long.valueOf(ownerId).equals(item.getOwner().getId())) {
            log.debug(String.valueOf(HandlerMessages.SERVER_ERROR));
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }

        try {
            Item itemPatched = applyPatchToItem(patch, item);
            User owner = userService.getById(Long.valueOf(ownerId));
            itemPatched.setOwner(owner);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), itemPatched);
            return itemMapper.toItemDtoRes(itemRepository.save(itemPatched));
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
        } catch (NotFoundException e) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }
    }

    public Item applyPatchToItem(JsonMergePatch patch, Item targetCustomer)
            throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, Item.class);
    }

    public List<ItemDtoRes> getAllByUserId(Long userId) {
        List<ItemDtoRes> itemDtoRes1 = itemRepository.findAll().stream().filter(e -> e.getOwner().getId().equals(userId))
                .map(item -> itemMapper.toItemDtoRes(item)).collect(toList());

        List<ItemDtoRes> itemDtoRes = itemDtoRes1.stream().map(e -> getById(e.getId(), userId)).collect(toList());
        return itemDtoRes;
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

    @Transactional
    public Comment addComment(Comment comment1, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).get();
        User user = userRepository.findById(userId).get();

        if (!bookingRepository.existsBookingByBookerAndItemAndStatus(user, item, APPROVED))
            throw new ValidationException(String.valueOf(HandlerMessages.VALID));

        if (bookingRepository.findByBookerAndItemAndEndBefore(user, item, LocalDateTime.now()).isEmpty())
            throw new ValidationException(String.valueOf((HandlerMessages.VALID)));

        if (item.getOwner().getId().equals(userId)) {
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
