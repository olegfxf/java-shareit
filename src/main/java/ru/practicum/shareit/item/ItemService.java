package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.abstracts.AbstractService;
import ru.practicum.shareit.abstracts.AbstractStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.LogMessages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ItemService extends AbstractService<Item, AbstractStorage<Item>> {
    @Autowired
    public ItemService(ItemDLAStorage itemDLAStorage) {
        super(itemDLAStorage);
    }

//    @Override
//    public Item save(Item item) {
//        Item item1 = abstractStorage.save(item);
//        log.debug(String.valueOf(LogMessages.ADD), item);
//        return item1;
//    }

//    @Override
//    public List<Item> getAll(Long id) {
//        log.debug(String.valueOf(LogMessages.GET_ALL_USERS), abstractStorage.getALL());
//        return abstractStorage.getAll(id);
//    }

    public List<Item> search(String text) {
        log.debug(String.valueOf(LogMessages.TRY_GET_SEARCH));
        Set<Item> items = new HashSet<>(abstractStorage.getALL().stream().filter(item -> item.getAvailable()).filter(item ->
                item.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList()));
        Set<Item> items1 = new HashSet<>(abstractStorage.getALL().stream().filter(item -> item.getAvailable()).filter(item ->
                item.getDescription().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList()));
        items.addAll(items1);
        return new ArrayList<>(items);
    }

}
