package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.SmallBooking;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;
    UserService userService;
    ItemMapper itemMapper;
    BookingRepository bookingRepository;
    ItemRepository itemRepository;
    CommentRepository commentRepository;
    SmallBooking smallBooking;
    CommentMapper commentMapper;

    @Autowired
    public ItemController(ItemService itemService,
                          ItemMapper itemMapper,
                          BookingRepository bookingRepository,
                          ItemRepository itemRepository,
                          SmallBooking smallBooking,
                          CommentRepository commentRepository,
                          CommentMapper commentMapper,
                          UserService userService) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.smallBooking = smallBooking;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }


    @PostMapping
    @ResponseBody
    public ItemDtoRes save(@Valid @RequestBody ItemDtoReq itemDtoReq,
                           @RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {

        if (!userService.getAll().stream().anyMatch(e -> Long.valueOf(ownerId).equals(e.getId()))) {
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }

        if (itemService.getAll().stream().anyMatch(e -> itemDtoReq.getName().equals(e.getName())))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        //throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));


        Item item = itemMapper.toItem(itemDtoReq, Long.valueOf(ownerId));
        User owner = userService.getById(Long.valueOf(ownerId));
        item.setOwner(owner);
        log.debug(String.valueOf(LogMessages.TRY_ADD), item);
        return itemMapper.toItemDtoRes(itemService.save(item));

    }

    @GetMapping
    public List<ItemDtoRes> getAll(@RequestHeader("x-sharer-user-id") @NotEmpty String userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "вещей");
        return itemService.getAllByOwnerId1(Long.valueOf(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDtoRes getById(@PathVariable Long itemId, @RequestHeader("x-sharer-user-id") @NotEmpty String userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        return itemService.getById(itemId, Long.valueOf(userId));
    }


    @PutMapping
    public Item update(@Valid @RequestBody Item item) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), item);
        return itemService.update(item);
    }


    @DeleteMapping("/{itemId}")
    public Item removeById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), itemId);
        return itemService.removeById(itemId);
    }


    @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Item> updateItem(@PathVariable Long id,
                                           @RequestBody JsonMergePatch patch,
                                           @RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {

        try {
            Item item = itemService.getById(id);
            Item itemPatched = applyPatchToItem(patch, item);

            if (!Long.valueOf(ownerId).equals(item.getOwner().getId())) {
                log.debug(String.valueOf(HandlerMessages.SERVER_ERROR));
                throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
            }

            User owner = userService.getById(Long.valueOf(ownerId));
            itemPatched.setOwner(owner);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), itemPatched);

            itemService.update(itemPatched);
            return ResponseEntity.ok(itemPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Item applyPatchToItem(
            JsonMergePatch patch, Item targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, Item.class);
    }

    @GetMapping("/search")
    public List<Item> search(@RequestParam String text) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH), text);
        if (text.isEmpty()) return new ArrayList<>();
        return itemService.search(text);
    }

    @PostMapping("{itemId}/comment")
    @ResponseBody
    public CommentDtoRes addComment(@Valid @RequestBody CommentDtoReq commentDtoReq,
                           @RequestHeader("x-sharer-user-id") @NotEmpty String userId,
                           @PathVariable Long itemId) {

        System.out.println("@@@@1 " + commentDtoReq.getText());
        return commentMapper.toCommentDtoRes(itemService.addComment(commentMapper.toComment(commentDtoReq), itemId, Long.valueOf(userId)));
    }
}
