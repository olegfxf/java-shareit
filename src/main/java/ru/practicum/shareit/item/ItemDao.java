package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
public class ItemDao {
    static Map<Long, Item> mem = new HashMap<>();
    static Long id = 0L;

    public List<Item> getALL(){
        return mem.values().stream().collect(toList());
    }

    public Item save(Item item) {
        item.setId(id++);
        return mem.put(id, item);
    }

    public static Item getById(Long id) {
        return mem.get(id);
    }

    public Item removeById(Long itemId) {
        return mem.remove(itemId);
    }

//    public User update(){
//        return mem.
//    }

    public static Item update(Item item){
        Long id = item.getId();
        if (mem.get(getById(id)) == null)
            throw new NotFoundException(String.valueOf(ExceptionMessages.NOT_FOUND_ID));
        mem.put(id,item);
        return item;
    }

}

