package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDtoRes;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ItemDtoResTest {
    @Autowired
    JacksonTester<ItemDtoRes> json;

    ItemDtoRes itemDtoRes = ItemDtoRes.builder()
            .id(0L)
            .name("name")
            .description("description")
            .available(true)
            .ownerId(1L)
            .requestId(1L)
            .build();

    @Test
    void jsonItemDtoRes() throws IOException {
        JsonContent<ItemDtoRes> result = json.write(itemDtoRes);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(itemDtoRes.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemDtoRes.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(itemDtoRes.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDtoRes.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(itemDtoRes.getOwnerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(itemDtoRes.getRequestId().intValue());
    }
}
