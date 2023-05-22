package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoResTest {

    @Autowired
    JacksonTester<BookingDtoRes> json;

    BookingDtoRes bookingDtoRes = BookingDtoRes.builder()
            .id(1L)
            .start(LocalDateTime.of(2023, 5, 18, 22, 50))
            .end(LocalDateTime.of(2023, 5, 18, 23, 0))
            .item(new Item())
            .booker(new User())
            .status(Status.APPROVED)
            .build();

    @Test
    void jsonBookingDtoRes() throws Exception {
        JsonContent<BookingDtoRes> result = json.write(bookingDtoRes);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-05-18T22:50:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2023-05-18T23:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(new Item().getId());
        assertThat(result).extractingJsonPathNumberValue("$.user.id").isEqualTo(new User().getId());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }

}
