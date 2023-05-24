package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTestMockito {
    @InjectMocks
    ItemService itemService;

    @Mock
    ItemRepository itemRepository;


    @Test
    void addItem() {
    }

    @Test
    void updateItem() {
    }


    @Test
    void getAllByUserId() {
    }

    @Test
    void searchText() {
        List<Item> expectedItems = new ArrayList<>();
        String text = "text";
        when(itemRepository.search(text))
                .thenReturn(expectedItems);

        List<Item> actualItems = itemService.searchText(text).stream()
                .map(e -> ItemMapper.toItemFromDtoRes(e)).collect(Collectors.toList());

        assertEquals(expectedItems, actualItems);
    }

    @Test
    void getById() {
        long itemId = 0L;
        Item expectedItem = new Item();
        when(itemRepository.findById(itemId))
                .thenReturn(Optional.of(expectedItem));

        Item actualItem = itemService.getById(itemId);

        assertEquals(expectedItem, actualItem);
    }


    @Test
    void addComment() {
    }

    @Test
    void removeById1() {
        long itemId = 0L;
        itemRepository.deleteById(itemId);

        verify(itemRepository, times(1)).deleteById(eq(itemId));
    }
}