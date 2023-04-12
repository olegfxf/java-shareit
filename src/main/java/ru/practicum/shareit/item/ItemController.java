package ru.practicum.shareit.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {
    public ItemService itemService;
    public ItemDao itemDao;
    @Autowired
    public ItemController(ItemService itemService, ItemDao itemDao) {
        this.itemService = itemService;
        this.itemDao = itemDao;
    }
    @GetMapping
    public List<Item> getAll(){
        log.debug(String.valueOf(LogMessages.TRY_GET_ALL), "пользователей");
        return itemService.getAll();
    }
    @PostMapping
    @ResponseBody
    public  Item save(@Valid @RequestBody Item item){
        log.debug(String.valueOf(LogMessages.TRY_ADD), item);
        return itemService.save(item);
    }
    @PutMapping
    public Item update(@Valid @RequestBody Item item){
        log.debug(String.valueOf(LogMessages.TRY_UPDATE),item);
        return itemService.update(item);
    }
    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), itemId);
        //System.out.println("/{userId} get");
        return itemService.getById(itemId);
    }

    @DeleteMapping("/{itemId}")
    public Item removeById(@PathVariable Long itemId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE), itemId);
        System.out.println("/{itemId} del");
        return itemService.removeById(itemId);
    }


            @PatchMapping(path = "/{id}", consumes = "application/json")
    public ResponseEntity<Item> updateCustomer(@PathVariable Long id, @RequestBody JsonMergePatch patch) {
        System.out.println("############## Patch");
        try {
            Item customer = itemDao.getById(id);
            Item customerPatched = applyPatchToCustomer(patch, customer);
            ItemDao.update(customerPatched);
            return ResponseEntity.ok(customerPatched);
        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    private Item applyPatchToCustomer(
            JsonMergePatch patch, Item targetCustomer) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(new ObjectMapper().convertValue(targetCustomer, JsonNode.class));
        return new ObjectMapper().treeToValue(patched, Item.class);
    }
}
