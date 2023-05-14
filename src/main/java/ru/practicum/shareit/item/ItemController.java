package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/items")
public class ItemController {
    ItemService itemService;
    ItemMapper itemMapper = new ItemMapper();

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseBody
    public ItemDtoRes save(@Valid @RequestBody ItemDtoReq itemDtoReq,
                           @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), "item");
        return itemService.addItem(itemDtoReq, userId);
    }

    @GetMapping
    public List<ItemDtoRes> getAll(@RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "вещей");
        return itemService.getAllByUserId(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoRes getById(@PathVariable Long itemId, @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        return itemService.getById(itemId, userId);
    }

    @PutMapping
    public ItemDtoRes update(@Valid @RequestBody Item item) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), item);
        return itemMapper.toItemDtoRes(itemService.update(item));
    }

    @DeleteMapping("/{itemId}")
    public ItemDtoRes removeById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE_OBJECT), itemId);
        return itemService.removeById1(itemId);
    }

    @PatchMapping(path = "/{itemId}", consumes = "application/json")
    @ResponseBody
    public ItemDtoRes updateItem(@PathVariable Long itemId,
                                 @RequestBody JsonMergePatch patch,
                                 @RequestHeader("x-sharer-user-id") @NotNull Long userId) {
        return itemService.updateItem(itemId, patch, userId);
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
                                    @RequestHeader("x-sharer-user-id") @NotNull Long userId,
                                    @PathVariable Long itemId) {
        return CommentMapper.toCommentDtoRes(itemService.addComment(CommentMapper.toComment(commentDtoReq), itemId,userId));
    }
}
