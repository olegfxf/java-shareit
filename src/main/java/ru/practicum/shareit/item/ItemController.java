package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.sun.jdi.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
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
    ItemMapper itemMapper = new ItemMapper();

    @Autowired
    public ItemController(ItemService itemService,
                          UserService userService) {
        this.itemService = itemService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseBody
    public ItemDtoRes save(@Valid @RequestBody ItemDtoReq itemDtoReq,
                           @RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "item");
        return itemService.addItem(itemDtoReq, ownerId);
    }

    @GetMapping
    public List<ItemDtoRes> getAll(@RequestHeader("x-sharer-user-id") @NotEmpty String userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "вещей");
        return itemService.getAllByUserId(Long.valueOf(userId));
    }

    @GetMapping("/{itemId}")
    public ItemDtoRes getById(@PathVariable Long itemId, @RequestHeader("x-sharer-user-id") @NotEmpty String userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        return itemService.getById(itemId, Long.valueOf(userId));
    }

    @PutMapping
    public ItemDtoRes update(@Valid @RequestBody Item item) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), item);
        return itemMapper.toItemDtoRes(itemService.update(item));
    }

    @DeleteMapping("/{itemId}")
    public ItemDtoRes removeById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), itemId);
        return itemMapper.toItemDtoRes(itemService.removeById(itemId));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json")
    @ResponseBody
    public Item updateItem(@PathVariable Long id,
                                           @RequestBody JsonMergePatch patch,
                                           @RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {

        Item item = itemService.getById(id);


        if (!Long.valueOf(ownerId).equals(item.getOwner().getId())) {
            log.debug(String.valueOf(HandlerMessages.SERVER_ERROR));
            throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
        }


        try {
            Item itemPatched = applyPatchToItem(patch, item);
            User owner = userService.getById(Long.valueOf(ownerId));
            itemPatched.setOwner(owner);
            log.debug(String.valueOf(LogMessages.TRY_PATCH), itemPatched);
            System.out.println(itemPatched + "  KKK");

            itemService.update(itemPatched);
            return itemService.update(itemPatched);
           // return ResponseEntity.ok(itemPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            //return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            throw new InternalException(String.valueOf(HandlerMessages.SERVER_ERROR));
        } catch (NotFoundException e) {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }
        //return itemService.updateItem1(id, patch, ownerId);
    }

    private Item applyPatchToItem(
            JsonMergePatch patch, Item targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, Item.class);
    }

    @GetMapping("/search")
    public List<ItemDtoRes> search(@RequestParam String text) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH), text);
        if (text.isEmpty()) return new ArrayList<>();
        return itemService.searchText(text);
    }

    @PostMapping("{itemId}/comment")
    @ResponseBody
    public CommentDtoRes addComment(@Valid @RequestBody CommentDtoReq commentDtoReq,
                                    @RequestHeader("x-sharer-user-id") @NotEmpty String userId,
                                    @PathVariable Long itemId) {
        return CommentMapper.toCommentDtoRes(itemService.addComment(CommentMapper.toComment(commentDtoReq), itemId, Long.valueOf(userId)));
    }
}
