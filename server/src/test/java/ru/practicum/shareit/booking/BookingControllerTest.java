package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoRes;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookingService bookingService;

    private BookingDtoRes bookingDtoRes;

    Long userId;

    Long bookingId;


    @BeforeEach
    void setUp() {
        userId = 1L;
        bookingId = 1L;
        Item item = new Item();
        item.setId(1L);
        item.setName("itemName");
        User booker = new User();
        booker.setId(1L);
        booker.setName("bookerName");

        bookingDtoRes = BookingDtoRes.builder()
                .id(1L)
                .start(LocalDateTime.of(2023, 05, 19, 10, 10))
                .end(LocalDateTime.of(2023, 06, 19, 10, 10))
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build();
    }

    @SneakyThrows
    @Test
    void save() {
        long userId = 1L;
        when(bookingService.save(any(Long.class), any()))
                .thenReturn(bookingDtoRes);

        mvc.perform(post("/bookings/")
                        .content(mapper.writeValueAsString(bookingDtoRes))
                        .header("x-sharer-user-id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoRes.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoRes.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingDtoRes.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDtoRes.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDtoRes.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoRes.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDtoRes.getStatus().toString())));
    }

    @SneakyThrows
    @Test
    void approveIt() {
        when(bookingService.approveIt(any(Long.class), any(Long.class), any(Boolean.class)))
                .thenReturn(bookingDtoRes);

        mvc.perform(patch("/bookings/1", bookingId)
                        .content(mapper.writeValueAsString(bookingDtoRes))
                        .header("x-sharer-user-id", userId)
                        .queryParam("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoRes.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoRes.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingDtoRes.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDtoRes.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDtoRes.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoRes.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDtoRes.getStatus().toString())));

    }

    @SneakyThrows
    @Test
    void getById() {
        long userId = 1L;
        long bookingId = 1L;
        when(bookingService.getById(bookingId, userId))
                .thenReturn(bookingDtoRes);

        mvc.perform(get("/bookings/{bookingId}", bookingId)
                        .content(mapper.writeValueAsString(bookingDtoRes))
                        .header("x-sharer-user-id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDtoRes.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDtoRes.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.item.id", is(bookingDtoRes.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.item.name", is(bookingDtoRes.getItem().getName())))
                .andExpect(jsonPath("$.booker.id", is(bookingDtoRes.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDtoRes.getBooker().getName())))
                .andExpect(jsonPath("$.status", is(bookingDtoRes.getStatus().toString())));
    }

    @SneakyThrows
    @Test
    void getAll() {
        long userId = 1L;
        List<BookingDtoRes> expectedItems = List.of(bookingDtoRes);

        Mockito
                .when(bookingService.getAll(userId, State.ALL.toString(), 0, 20))
                .thenReturn(expectedItems);

        mvc.perform(get("/bookings")
                        .content(mapper.writeValueAsString(expectedItems))
                        .header("x-sharer-user-id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDtoRes.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(bookingDtoRes.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDtoRes.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDtoRes.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDtoRes.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDtoRes.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingDtoRes.getStatus().toString())));
    }

    @SneakyThrows
    @Test
    void ownerGet() {
        long userId = 1L;
        List<BookingDtoRes> expectedItems = List.of(bookingDtoRes);

        Mockito
                .when(bookingService.ownerGet(userId, State.ALL.toString(), 0, 20))
                .thenReturn(expectedItems);

        mvc.perform(get("/bookings/owner")
                        .content(mapper.writeValueAsString(expectedItems))
                        .header("x-sharer-user-id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDtoRes.getId()), Long.class))
                .andExpect(jsonPath("$.[0].start", is(bookingDtoRes.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].end", is(bookingDtoRes.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$.[0].item.id", is(bookingDtoRes.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.[0].item.name", is(bookingDtoRes.getItem().getName())))
                .andExpect(jsonPath("$.[0].booker.id", is(bookingDtoRes.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.[0].booker.name", is(bookingDtoRes.getBooker().getName())))
                .andExpect(jsonPath("$.[0].status", is(bookingDtoRes.getStatus().toString())));
    }
}