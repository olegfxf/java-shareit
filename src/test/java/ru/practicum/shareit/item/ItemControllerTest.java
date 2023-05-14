package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoRes;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {
    @Autowired
    ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private MockMvc mvc;

    @Captor
    private ArgumentCaptor<Long> itemArgumentCaptor;

    private ItemDtoRes itemDtoRes;

    @BeforeEach
    void setUp() {
        itemDtoRes = ItemDtoRes.builder()
                .id(1)
                .name("Дрель")
                .description("Малогабаритная дрель")
                .available(true)
                .ownerId(2L)
                .requestId(5L)
                .build();
    }

    @SneakyThrows
    @Test
    void save() {
        when(itemService.addItem(any(), anyLong()))
                .thenReturn(itemDtoRes);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRes.getId()), Long.class))
                .andDo(print())
                .andExpect(jsonPath("$.name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoRes.getAvailable())));

    }

    @SneakyThrows
    @Test
    void getAll() {
        List<ItemDtoRes> expectedItems = List.of(itemDtoRes);

        Mockito.when(itemService.getAllByUserId(any())).thenReturn(expectedItems);

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(expectedItems))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.[0].description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.[0].available", is(itemDtoRes.getAvailable())));
    }

    @SneakyThrows
    @Test
    void getById() {
        Long itemId = 1L;
        Long userId = 1L;
        when(itemService.getById(itemId, userId))
                .thenReturn(itemDtoRes);

        mvc.perform(get("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoRes.getAvailable())));
    }

    @SneakyThrows
    @Test
    void update() {
        Long userId = 1L;
        User user = new User();
        Long itemId = 1L;
        Item oldItem = Item.builder()
                .id(1L)
                .name("Дрель")
                .description("Малогабаритная дрель")
                .available(true)
                .owner(user)
                .requestId(5L)
                .build();

        when(itemService.updateItem(anyLong(), any(), anyLong())).thenReturn(itemDtoRes);

        mvc.perform(patch("/items/{userId}", userId)
                        .content(mapper.writeValueAsString(itemDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoRes.getAvailable())));

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(oldItem));

        Item item1 = itemRepository.findById(itemId).get();

        verify(itemRepository, Mockito.times(1))
                .findById(itemArgumentCaptor.capture());
        Long savedItem = itemArgumentCaptor.getValue();

        System.out.println(savedItem + "  savedItem");


    }

    @SneakyThrows
    @Test
    void removeById() {
        Long itemId = 1L;
        when(itemService.removeById1(itemId))
                .thenReturn(itemDtoRes);

        mvc.perform(delete("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoRes.getAvailable())));
        //.andDo(print());
    }

    @SneakyThrows
    @Test
    void updateItem() {
        Long itemId = 1L;
        when(itemService.updateItem(any(), any(), any()))
                .thenReturn(itemDtoRes);

        mvc.perform(patch("/items/{itemId}", itemId)
                        .content(mapper.writeValueAsString(itemDtoRes))
                        .header("x-sharer-user-id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoRes.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoRes.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoRes.getAvailable())));
    }

    @SneakyThrows
    @Test
    void search() {
    }

    @SneakyThrows
    @Test
    void addComment() {
    }
}