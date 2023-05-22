package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoRes;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class BookingDtoReqTest {

    @Autowired
    JacksonTester<BookingDtoReq> json;

    @Autowired
    JacksonTester<ItemDtoRes> json1;

    ItemDtoRes itemDtoRes = ItemDtoRes.builder()
            .id(0L)
            .name("name")
            .description("description")
            .available(true)
            .ownerId(1L)
            .requestId(1L)
            .build();

    BookingDtoReq bookingDtoReq = BookingDtoReq.builder()
            .id(1L)
            .start(LocalDateTime.of(2023, 5, 18, 22, 50))
            .end(LocalDateTime.of(2023, 5, 18, 23, 0))
            .itemId(1L)
            .bookerId(1L)
            .build();

    @Test
    void jsonBookingDtoReq() throws Exception {
        JsonContent<BookingDtoReq> result = json.write(bookingDtoReq);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-05-18T22:50:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-05-18T23:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(bookingDtoReq.getItemId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(bookingDtoReq.getBookerId().intValue());
    }
}
