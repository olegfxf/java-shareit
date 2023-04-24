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
import ru.practicum.shareit.abstracts.AbstractDLAStorage;
import ru.practicum.shareit.abstracts.AbstractStorage;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDtoReq;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.HandlerMessages;
import ru.practicum.shareit.messages.LogMessages;
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
    AbstractStorage<Item> itemStorage;
    AbstractStorage<User> userStorage;

    @Autowired
    public ItemController(ItemService itemService,
                          AbstractDLAStorage<Item> itemDLAStorage,
                          AbstractDLAStorage<User> userStorage) {
        this.itemService = itemService;
        this.itemStorage = itemDLAStorage;
        this.userStorage = userStorage;
    }


    @PostMapping
    @ResponseBody
    public ItemDtoRes save(@Valid @RequestBody ItemDtoReq itemDtoReq,
                           @RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {

//        System.out.println(optionalHeader.isPresent() ? "Yes" : "_No");

//        if (!optionalHeader.isPresent())
//            throw new InternalException(String.valueOf(HandlerMessages.ERROR_500));

//        final Long ownerId = Long.valueOf(optionalHeader.get());
//        System.out.println(ownerId);

        if (!userStorage.getALL().stream().anyMatch(e -> Long.valueOf(ownerId).equals(e.getId()))) {
//            System.out.println("^^^");
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        }


        if (itemStorage.getALL().stream().anyMatch(e -> itemDtoReq.getName().equals(e.getName())))
            throw new NotFoundException(String.valueOf(HandlerMessages.NOT_FOUND));
        //throw new ConflictException(String.valueOf(HandlerMessages.CONFLICT));


        Item item = itemDtoReq.toItem();
        item.setOwner(Long.valueOf(ownerId));
        log.debug(String.valueOf(LogMessages.TRY_ADD), item);
        return new ItemDtoRes(itemService.save(item));

    }

    @GetMapping
    public List<Item> getAll(@RequestHeader("x-sharer-user-id") @NotEmpty String ownerId) {
//        if (headers.get("x-sharer-user-id") == null)
//            throw new InternalException(String.valueOf(HandlerMessages.ERROR_500));
//        List<String> itemIds = headers.get("x-sharer-user-id");
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "вещей");
        return itemService.getAll(Long.valueOf(ownerId));
    }

    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        return itemService.getById(itemId);
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

//        if (headers.get("x-sharer-user-id") == null)
//            throw new InternalException(String.valueOf(HandlerMessages.ERROR_500));


        try {
            Item item = itemStorage.getById(id);
            Item itemPatched = applyPatchToItem(patch, item);

            //           List<String> itemIds = headers.get("x-sharer-user-id");

            if (!Long.valueOf(ownerId).equals(item.getOwner())) {
                log.debug(String.valueOf(HandlerMessages.SERVER_ERROR));
                throw new NotFoundException(ExceptionMessages.NOT_FOUND_ID);
            }

            itemPatched.setOwner(Long.valueOf(ownerId));
            log.debug(String.valueOf(LogMessages.TRY_PATCH), itemPatched);

            itemStorage.update(itemPatched);
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
        if (text.equals("")) return new ArrayList<>();
        return itemService.search(text);
    }
}
